package com.example.demo.service;

import com.example.demo.base.CommonResponseCode;
import com.example.demo.base.PageResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.modal.FileInfoResponse;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final MinioClient minioClient;


    @Value("${minio.bucket-name}")
    private String bucketName;

    public ResponseEntity<ResponseBase<Object>> uploadFile(MultipartFile file) {
        try {
            String objectName = file.getOriginalFilename();
            if (!fileExists(objectName)) {
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                return ResponseEntity.noContent().build();
            } else {
                throw new RuntimeException(CommonResponseCode.EXISTED_DATA.getMessage() + " file: " + objectName);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException |
                 InsufficientDataException | InternalException | InvalidResponseException |
                 ServerException | XmlParserException | IOException e) {
            log.error(CommonResponseCode.MEDIA_UPLOAD_FAILED.getMessage(), e);
            throw new RuntimeException(CommonResponseCode.MEDIA_UPLOAD_FAILED.getMessage());
        }
    }


    public ResponseEntity<ResponseBase<PageResponse<List<String>>>> listFiles(Pageable pageable) {
        try {
            List<String> objectNames = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder().bucket(bucketName).build()
            );
            for (Result<Item> result : results) {
                Item item = result.get();
                objectNames.add(item.objectName());
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), objectNames.size());
            List<String> paginatedObjectNames = objectNames.subList(start, end);
            Page<String> page = new PageImpl<>(paginatedObjectNames, pageable, objectNames.size());
            PageResponse<List<String>> pageResponse = new PageResponse<>();
            pageResponse.setPage(page.getNumber());
            pageResponse.setSize(page.getSize());
            pageResponse.setTotalElements((int) page.getTotalElements());
            pageResponse.setTotalPages(page.getTotalPages());
            pageResponse.setData(Collections.singletonList(page.getContent()));

            return ResponseEntity.ok(new ResponseBase<>(pageResponse));
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException |
                 InternalException | InvalidResponseException | NoSuchAlgorithmException |
                 ServerException | XmlParserException | IOException e) {
            log.error(CommonResponseCode.FAILED.getMessage(), e);
            throw new RuntimeException(CommonResponseCode.FAILED.getMessage());
        }
    }


    public ResponseBase<FileInfoResponse> getFileInfo(String objectName) {
        try {
            if (fileExists(objectName)) {
                StatObjectResponse stat = minioClient.statObject(
                        StatObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                );

                FileInfoResponse fileInfo = new FileInfoResponse();
                fileInfo.setFilename(objectName);
                fileInfo.setSize(stat.size());

                GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(7 * 24 * 60 * 60)
                        .build();

                fileInfo.setUrl(minioClient.getPresignedObjectUrl(urlArgs));

                return new ResponseBase<>(fileInfo);
            }
        } catch (InvalidKeyException | ErrorResponseException | InsufficientDataException |
                InternalException | InvalidResponseException | NoSuchAlgorithmException |
                ServerException | XmlParserException | IOException e) {
            log.error(CommonResponseCode.FAILED.getMessage(), e);
            throw new RuntimeException(CommonResponseCode.FAILED.getMessage());
        }
        return null;
    }



    public ResponseEntity<Object> deleteFile(String objectName) {
        try {
            if (fileExists(objectName)) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build()
                );
                return ResponseEntity.noContent().build();
            } else {
                throw new RuntimeException(CommonResponseCode.NOT_EXISTED.getMessage() + " file: " + objectName);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException |
                 InsufficientDataException | InternalException | InvalidResponseException |
                 ServerException | XmlParserException | IOException e) {
            log.error(CommonResponseCode.FAILED.getMessage(), e);
            throw new RuntimeException(CommonResponseCode.FAILED.getMessage());
        }
    }

    private boolean fileExists(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return stat != null;
        } catch (Exception e) {
            throw new RuntimeException(CommonResponseCode.NOT_EXISTED.getMessage() + " file: " + objectName);
        }
    }
}
