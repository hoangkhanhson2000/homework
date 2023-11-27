package com.example.demo.controller;

import com.example.demo.modal.RoleRequest;
import com.example.demo.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Admin")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-role")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        adminService.createRole(roleRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-permission/{roleId}")
    public ResponseEntity<?> updatePermissions(@PathVariable Long roleId, @RequestBody List<String> permissions) {
        adminService.updatePermissions(roleId, permissions);
        return ResponseEntity.ok().build();
    }
}

