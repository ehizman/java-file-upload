package com.ehizman.javafileupload.services;

import com.ehizman.javafileupload.exceptions.StorageException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class FileStoreServiceImplTest {
    FileStoreService fileStoreService = new FileStoreServiceImpl();

    @Test
    void testThatCanStoreFile() throws StorageException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        String generatedFileUrl = fileStoreService.storeFile(file);
        assertThat(generatedFileUrl).isNotNull();
    }

    @Test
    void testThatCanLoadAnExistingFile() throws StorageException, IOException {
        //create a file object pointing to a folder called 'uploaded_files' in the project's root directory
        File theDir = new File(".//uploaded_files");
        //the directory does not exist, then create it
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        //create a test file called "hello.txt"
        File file = new File(".//uploaded_files/hello.txt");
        file.createNewFile();

        // load the test file using the fileStoreService
        Resource resource = fileStoreService.load("hello.txt");
        assertThat(resource).isNotNull();

        // delete the test directory along with the created test file
        FileUtils.deleteDirectory(theDir);
    }

    @Test
    void testThatThrowsStorageExceptionWhenFileIsNotExisting() throws IOException {
        //files are loaded from the "uploaded_files" directory in the project root folder
        // check if the "uploaded_files" directory exists, if it exists, then delete it
        File directory = new File(".//uploaded_files");
        if (directory.exists()){
            FileUtils.deleteDirectory(directory);
        }
        assertThatThrownBy(()-> fileStoreService.load("test.pdf"))
                .isInstanceOf(StorageException.class).hasMessage("Could not find the file!");
    }
}