package com.ehizman.javafileupload.services;

import com.ehizman.javafileupload.exceptions.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileStoreServiceImpl  implements FileStoreService{
    private final Path rootLocation = Paths.get("uploaded_files/");

    public String storeFile(MultipartFile file) throws StorageException {
        try {
            //check if file is empty
            if (file.isEmpty()) {
                throw new StorageException("Cannot store empty file");
            }
            //if file is not empty get the file extension
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uploadedFileName = UUID.randomUUID() + "." + extension;
            //create a file object pointing to a folder called 'uploaded_files' in the project's root directory
            File theDir = new File(".//uploaded_files");
            //the directory does not exist, then create it
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            Path destinationFile = rootLocation.resolve(Paths.get(uploadedFileName)).normalize().toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                log.info("File copied -> {}", destinationFile);
                final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                return baseUrl + "/fileUpload/files/" +
                        uploadedFileName;
            }
        }
        catch (IOException e) {
            log.info("Exception --> {}", e.getMessage());
            throw new StorageException("cannot save file");
        }
    }

    @Override
    public Resource load(String filename) throws StorageException {
        try {
            // read the file based on the filename
            Path file = rootLocation.resolve(filename);
            // get resource from path
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not find the file!");
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() throws StorageException {
        //load all files
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            log.info("Exception occured --> {}", e.getMessage());
            throw new StorageException(String.format("Failed to read directory due to nested exception %s", e.getMessage()));
        }
    }

}
