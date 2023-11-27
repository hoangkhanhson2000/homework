package com.example.demo.controller;

import com.example.demo.entity.Roles;
import com.example.demo.entity.RolePermission;
import com.example.demo.entity.Users;
import com.example.demo.entity.UserRole;
import com.example.demo.modal.LoginRequest;
import com.example.demo.modal.RegisterRequest;
import com.example.demo.modal.RoleRequest;
import com.example.demo.modal.UserInfoResponse;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.security.JwtResponse;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isEmpty()) {
            Users users = new Users();

            users.setUsername(request.getUsername());
            users.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(users);

            Roles defaultRoles = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            UserRole userRole = new UserRole();
            userRole.setUsers(users);
            userRole.setRoles(defaultRoles);
            userRoleRepository.save(userRole);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Username already exists");
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isEmpty()) {
            Users users = new Users();

            users.setUsername(request.getUsername());
            users.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(users);

            Roles defaultRoles = roleRepository.findByRoleName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
            UserRole userRole = new UserRole();
            userRole.setUsers(users);
            userRole.setRoles(defaultRoles);
            userRoleRepository.save(userRole);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Username already exists");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = userDetails.getUsername();

        List<UserRole> userRoles = userRoleRepository.findByUsers(userRepository.findByUsername(username).orElse(null));

        List<String> permissions = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoles(userRole.getRoles());
            permissions.addAll(rolePermissions.stream().map(RolePermission::getPermission).toList());
        }

        UserInfoResponse userInfoResponse = new UserInfoResponse(username, "", userRoles.stream().map(userRole -> userRole.getRoles().getRoleName()).collect(Collectors.toList()), permissions);

        return ResponseEntity.ok(userInfoResponse);
    }
    @PostMapping("/create-role")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        Roles role = new Roles();
        role.setRoleName(roleRequest.getRoleName());
        roleRepository.save(role);

        for (String permission : roleRequest.getPermissions()) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoles(role);
            rolePermission.setPermission(permission);
            rolePermissionRepository.save(rolePermission);
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-permission/{roleId}")
    @Transactional
    public ResponseEntity<?> updatePermissions(@PathVariable Long roleId, @RequestBody List<String> permissions) {
        Roles role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        List<RolePermission> existingPermissions = rolePermissionRepository.findByRoles(role);

        existingPermissions.removeIf(permission -> !permissions.contains(permission.getPermission()));
        rolePermissionRepository.deleteAll(existingPermissions);

        for (String permission : permissions) {
            if (existingPermissions.stream().noneMatch(p -> p.getPermission().equals(permission))) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoles(role);
                rolePermission.setPermission(permission);
                rolePermissionRepository.save(rolePermission);
            }
        }

        return ResponseEntity.ok().build();
    }



}
