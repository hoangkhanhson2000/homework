package com.example.demo.entity;

import lombok.Data;

@Data
public class FileInfo {
    private String filename;
    private Long size;
    private String url;
}