package com.ehizman.javafileupload.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileInfo {
    private String fileName;
    private String fileURL;
}
