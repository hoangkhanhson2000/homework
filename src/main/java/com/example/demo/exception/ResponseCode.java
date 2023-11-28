package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public enum ResponseCode implements IResponseCode {

    USER_NOT_FOUND(1404, "User not found", HttpStatus.NOT_FOUND),
    USER_UNAUTHORIZED(1403, "Unauthorized", HttpStatus.UNAUTHORIZED),
    PASSWORDS_NOT_MATCH(1000, "Password not match"),
    USER_EXISTED(1001, "Username Existed"),

    ROLE_NOT_FOUND(2404, "Role not found"),
    ROLE_EXISTED(2401, "Role Existed");
    private final int code;
    private final String message;
    private final HttpStatus type;

    ResponseCode(int code, String message, HttpStatus type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    ResponseCode(int code, String message) {
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