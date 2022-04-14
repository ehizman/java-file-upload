package com.ehizman.javafileupload.controller;

import com.ehizman.javafileupload.controller.responses.APIResponse;
import com.ehizman.javafileupload.exceptions.StorageException;
import com.ehizman.javafileupload.models.FileInfo;
import com.ehizman.javafileupload.services.FileStoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("api/v1/file")
@CrossOrigin("")
public class FileUploadController {
    private final FileStoreService fileStoreService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws StorageException {
            return new ResponseEntity<>(new APIResponse(fileStoreService.storeFile(file), true, 200)
                                                            , HttpStatus.OK);
    }
    // get file by filename
    @GetMapping("/get/{filename:.+}")
    public ResponseEntity<?> getFile(@PathVariable String filename) throws IOException, StorageException {
        Resource file = fileStoreService.load(filename);
        log.info("Resource --> {}", file.getFile().getAbsolutePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    //get all files
    @GetMapping("/all")
    public ResponseEntity<?> getAllFiles() throws IOException, StorageException {
        List<FileInfo> fileInfoList = fileStoreService.loadAll()
                                                        .map(path ->{
                                                            String fileName = path.getFileName().toString();
                                                            String url = MvcUriComponentsBuilder.fromMethodName(
                                                                    FileUploadController.class,
                                                                    "getFile",
                                                                    path.getFileName().toString()).build().toString();
                                                            return new FileInfo(fileName, url);
                                                        } ).collect(Collectors.toList());
        return new ResponseEntity<>(fileInfoList, HttpStatus.OK);
    }
}
