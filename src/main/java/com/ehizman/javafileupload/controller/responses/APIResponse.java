package com.ehizman.javafileupload.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class APIResponse {
    private String message;
    private boolean isSuccessful;
    private int statusCode;
}
