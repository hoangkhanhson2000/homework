package com.example.demo.modal;

import lombok.Data;

@Data
public class FileInfoResponse {
    private String filename;
    private Long size;
    private String url;
}