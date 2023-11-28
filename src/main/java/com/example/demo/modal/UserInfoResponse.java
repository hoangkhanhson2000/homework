package com.example.demo.modal;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private String username;
    private String email;
    private List<String> roles;
    private List<String> permissions;

    public UserInfoResponse(String username, String email, List<String> roles, List<String> permissions) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.permissions = permissions;
    }
}
