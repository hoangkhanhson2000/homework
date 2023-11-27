package com.example.demo.service;

import com.example.demo.entity.RolePermission;
import com.example.demo.entity.Roles;
import com.example.demo.modal.RoleRequest;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createRole(RoleRequest roleRequest) {
        if (roleRepository.findByRoleName(roleRequest.getRoleName()).isPresent()) {
            throw new RuntimeException("Role with the same name already exists");
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
    }


    @Transactional
    public void updatePermissions(Long roleId, List<String> permissions) {
        Roles role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

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
    }

}
