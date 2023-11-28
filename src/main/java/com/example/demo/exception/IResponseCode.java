package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public interface IResponseCode {

    int getCode();
    String getMessage();
    HttpStatus getType();

}