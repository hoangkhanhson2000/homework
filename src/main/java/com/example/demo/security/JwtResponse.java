package com.example.demo.security;

import lombok.Data;
import lombok.Getter;


@Getter
public class JwtResponse {

    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
