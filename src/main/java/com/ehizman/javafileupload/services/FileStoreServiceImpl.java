package com.ehizman.javafileupload.services;

import com.ehizman.javafileupload.exceptions.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStoreServiceImpl  implements FileStoreService{
    private final Path rootLocation = Paths.get("uploaded_files/");
    public String storeFile(MultipartFile file) throws StorageException {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Cannot store empty file");
            }
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String uploadedFileName = UUID.randomUUID() + "." + extension;
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
            e.printStackTrace();
            throw new StorageException("cannot save file");
        }
    }

}
