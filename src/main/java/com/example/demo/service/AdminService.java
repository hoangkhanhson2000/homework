package com.example.demo.service;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.base.UpdatedResponse;
import com.example.demo.entity.RolePermission;
import com.example.demo.entity.Roles;
import com.example.demo.exception.ResponseCode;
import com.example.demo.modal.RoleRequest;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public ResponseEntity<ResponseBase<CreatedResponse>> createRole(RoleRequest roleRequest) {
        if (roleRepository.findByRoleName(roleRequest.getRoleName()).isPresent()) {
            throw new RuntimeException(String.valueOf(ResponseCode.ROLE_EXISTED.getMessage()));
        }

        Roles role = new Roles();
        role.setRoleName(roleRequest.getRoleName());
        roleRepository.save(role);

        for (String permission : roleRequest.getPermissions()) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoles(role);
            rolePermission.setPermission(permission);
            rolePermissionRepository.save(rolePermission);
        }

        return ResponseEntity.ok(new ResponseBase<>(new CreatedResponse(role.getRoleId())));
    }



    @Transactional
    public void updatePermissions(Long roleId, List<String> permissions) {
        Roles role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException(String.valueOf(

                                ResponseCode.ROLE_NOT_FOUND.getMessage())));

        List<RolePermission> existingPermissions = rolePermissionRepository.findByRoles(role);

        existingPermissions.removeIf(permission -> !permissions.contains(permission.getPermission()));

        for (RolePermission existingPermission : existingPermissions) {
            String permission = existingPermission.getPermission();
            if (!permissions.contains(permission)) {
                existingPermission.setPermission(permission);
                rolePermissionRepository.save(existingPermission);
            }
        }

        for (String permission : permissions) {
            if (existingPermissions.stream().noneMatch(p -> p.getPermission().equals(permission))) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoles(role);
                rolePermission.setPermission(permission);
                rolePermissionRepository.save(rolePermission);
            }
        }

        new UpdatedResponse(role.getRoleId());
    }


}
