package com.example.demo.modal;

import lombok.Data;

@Data

public class RegisterRequest {
    private String username;
    private String password;
    private String confirmPassword;
}
