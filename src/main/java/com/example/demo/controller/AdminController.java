package com.example.demo.controller;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.base.UpdatedResponse;
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
    public ResponseEntity<ResponseBase<CreatedResponse>> createRole(@RequestBody RoleRequest roleRequest) {
        return adminService.createRole(roleRequest);
    }



    @PostMapping("/update-permission/{roleId}")
    public ResponseEntity<ResponseBase<UpdatedResponse>> updatePermissions(@PathVariable Long roleId, @RequestBody List<String> permissions) {
        adminService.updatePermissions(roleId, permissions);
        return ResponseEntity.noContent().build();
    }

}

