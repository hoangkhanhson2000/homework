package com.example.demo.base;

import com.example.demo.exception.IResponseCode;
import org.springframework.http.HttpStatus;

public enum CommonResponseCode implements IResponseCode {
    // Common
    SUCCESS(0, "Success"),
    FAILED(1, "Failed"),
    COMMON_ERROR(2, "Common Error"),
    INVALID_PARAM(3, "Invalid param"),
    INVALID_SESSION(4, "Invalid session"),
    UNHANDLE_REQUEST(5, "Unhandle request", HttpStatus.INTERNAL_SERVER_ERROR),
    REQUEST_WAS_EXPIRED(6, "Request was expired", HttpStatus.UNAUTHORIZED),
    SECURITY_VIOLATION(7, "Security violation"),
    EXISTED_DATA(8, "Existed data"),
    RESPONSE_ENCRYPTING_FAILED(9, "Response encrypting failed"),
    ACCESS_DENIED(10, "Access denied"),
    INVALID_REFRESH_TOKEN(11, "Invalid refresh token"),
    THIRD_PARTY_API_ERROR(12, "Third party api error"),
    REQUEST_PENDING(13, "Request is pending"),
    REFRESH_TOKEN_INVALID(14, "Refresh token failed"),
    NOT_FOUND_DATA(15, "Not found data", HttpStatus.NOT_FOUND),
    NOT_EXISTED(16, "not existed"),
    OUT_OF_SCOPE(17, "Out of scope"),
    MEDIA_UPLOAD_FAILED(18, "Upload image failed"),
    INVALID_FILE_TYPE(19,"Invalid file type. Only image files (PNG, JPG, JPEG, etc.) are allowed.")
    ;

    private final int code;
    private final String message;
    private final HttpStatus type;

    CommonResponseCode(int code, String message, HttpStatus type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    CommonResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
        this.type = HttpStatus.OK;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getType() {
        return type;
    }


}