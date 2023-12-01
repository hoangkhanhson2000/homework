package com.example.demo.service;

import com.example.demo.base.CreatedResponse;
import com.example.demo.base.ResponseBase;
import com.example.demo.entity.RolePermission;
import com.example.demo.entity.Roles;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.Users;
import com.example.demo.exception.ResponseCode;
import com.example.demo.modal.RoleIdRequest;
import com.example.demo.modal.RoleRequest;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final RolePermissionRepository rolePermissionRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

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
    }

    @Transactional
    public void assignRoles(Long userId, RoleIdRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.valueOf(ResponseCode.USER_NOT_FOUND.getMessage())));

        for (Long roleId : request.getRoleIds()) {
            Roles role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException(String.valueOf(ResponseCode.ROLE_NOT_FOUND.getMessage())));

            if (user.getRoles().stream().noneMatch(r -> r.getRoleId().equals(roleId))) {
                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                userRoleRepository.save(userRole);
            }
        }
    }

    @Transactional
    public void removeRoles(Long userId,RoleIdRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.valueOf(ResponseCode.USER_NOT_FOUND.getMessage())));

        for (Long roleId : request.getRoleIds()) {
            Roles role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException(String.valueOf(ResponseCode.ROLE_NOT_FOUND.getMessage())));

            userRoleRepository.deleteByUserAndRole(user, role);
        }
    }
}
