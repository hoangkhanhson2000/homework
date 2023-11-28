package com.example.demo.base;

import com.example.demo.exception.IResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseBase<T> {

    private int code;
    private String message;
    private T data;

    public ResponseBase(T data) {
        this.data = data;
        this.message = CommonResponseCode.SUCCESS.getMessage();
        this.code = CommonResponseCode.SUCCESS.getCode();
    }

    public ResponseBase(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public ResponseBase(T data, int code, String message) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public ResponseBase(T data, IResponseCode responseCode) {
        this.data = data;
        this.message = responseCode.getMessage();
        this.code = responseCode.getCode();
    }
}