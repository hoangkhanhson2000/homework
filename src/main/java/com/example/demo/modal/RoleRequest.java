package com.example.demo.modal;

import lombok.Data;

import java.util.List;

@Data
public class RoleRequest {
    private String roleName;
    private List<String> permissions;
}
