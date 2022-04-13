package com.ehizman.javafileupload.controller;

import com.ehizman.javafileupload.exceptions.StorageException;
import com.ehizman.javafileupload.services.FileStoreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/file")
public class FileUploadController {
    private final FileStoreService fileStoreService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            return new ResponseEntity<>(fileStoreService.storeFile(file), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
