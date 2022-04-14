package com.ehizman.javafileupload.services;

import com.ehizman.javafileupload.exceptions.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStoreService {
    String storeFile(MultipartFile file)throws StorageException;
    Resource load(String filename) throws StorageException;
}
