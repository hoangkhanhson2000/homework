package com.example.demo.controller;

import com.example.demo.base.PageResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.modal.FileInfoResponse;
import com.example.demo.service.FileUploadService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseBase<Object>> uploadFile(
            @RequestParam("file") @Parameter(description = "Select a file to upload", required = true) MultipartFile file) {
        return fileUploadService.uploadFile(file);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseBase<PageResponse<List<String>>>> listFiles(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return fileUploadService.listFiles(pageable);
    }

    @GetMapping("/info/{objectName}")
    public ResponseEntity<ResponseBase<FileInfoResponse>> getFileInfo(@PathVariable String objectName) {
        return ResponseEntity.ok(fileUploadService.getFileInfo(objectName));
    }

    @DeleteMapping("/{filename}")
    public ResponseEntity<Object> deleteFile(@PathVariable String filename) {
        return fileUploadService.deleteFile(filename);

    }

    @GetMapping("/download/{objectName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String objectName) {
        return fileUploadService.downloadFile(objectName);
    }
}


