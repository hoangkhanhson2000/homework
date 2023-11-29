package com.example.demo.service;

import com.example.demo.base.ResponseBase;
import com.example.demo.entity.RolePermission;
import com.example.demo.entity.Roles;
import com.example.demo.entity.UserRole;
import com.example.demo.entity.Users;
import com.example.demo.exception.ResponseCode;
import com.example.demo.modal.LoginRequest;
import com.example.demo.modal.RegisterRequest;
import com.example.demo.modal.UserInfoResponse;
import com.example.demo.repository.RolePermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticateService {

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

    public ResponseEntity<ResponseBase<?>> registerUser(RegisterRequest request, String roleName) {
        if (userRepository.findByUsername(request.getUsername()).isEmpty()) {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(
                        new ResponseBase<>(
                                ResponseCode.PASSWORDS_NOT_MATCH.getCode(),
                                ResponseCode.PASSWORDS_NOT_MATCH.getMessage()));
            }
            Users users = new Users();
            users.setUsername(request.getUsername());
            users.setEmail(request.getEmail());
            users.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(users);
            Roles defaultRoles = roleRepository.findByRoleName(roleName).orElseThrow(() ->
                    new RuntimeException(String.valueOf(
                            ResponseCode.ROLE_NOT_FOUND)));

            UserRole userRole = new UserRole();
            userRole.setUser(users);
            userRole.setRole(defaultRoles);
            userRoleRepository.save(userRole);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(
                    new ResponseBase<>(
                            ResponseCode.USER_EXISTED.getCode(),
                            ResponseCode.USER_EXISTED.getMessage()));
        }
    }

    public ResponseEntity<ResponseBase<?>> loginUser(LoginRequest loginRequest, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            JwtResponse jwtResponse = new JwtResponse(jwt);
            return ResponseEntity.ok(new ResponseBase<>(jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ResponseBase<>(
                            ResponseCode.USERNAME_PASSWORD_INVALID.getCode(),
                            ResponseCode.USERNAME_PASSWORD_INVALID.getMessage()));
        }
    }


    public ResponseEntity<ResponseBase<UserInfoResponse>> getUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Users user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseBase<>(
                            ResponseCode.USER_NOT_FOUND.getCode(),
                            ResponseCode.USER_NOT_FOUND.getMessage()));
        }
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        List<String> permissions = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoles(userRole.getRole());
            permissions.addAll(rolePermissions.stream().map(RolePermission::getPermission).toList());
        }
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                username,
                user.getEmail(),
                userRoles.stream().map(userRole -> userRole.getRole().getRoleName()).collect(Collectors.toList()),
                permissions);
        return ResponseEntity.ok(new ResponseBase<>(userInfoResponse));
    }

}
