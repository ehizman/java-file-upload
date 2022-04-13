package com.ehizman.javafileupload.controller.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class APIResponse {
    private String message;
    private boolean isSuccessful;
    private int statusCode;
}
