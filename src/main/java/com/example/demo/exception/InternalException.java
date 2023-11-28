package com.example.demo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalException extends RuntimeException {

    private final IResponseCode responseCode;

    public InternalException(IResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return this.responseCode == null ? "unknown" : this.responseCode.getMessage();
    }

    public int getCode() {
        return this.responseCode == null ? 1 : this.responseCode.getCode();
    }

    public HttpStatus getType() {
        return this.responseCode == null ? HttpStatus.INTERNAL_SERVER_ERROR : this.responseCode.getType();
    }

}