package com.example.demo.controller;

import com.example.demo.modal.LoginRequest;
import com.example.demo.modal.RegisterRequest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthenticateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Authenticator")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Autowired
    private AuthenticateService authenticateService;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        return authenticateService.registerUser(request, "USER");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        return authenticateService.registerUser(request, "ADMIN");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return authenticateService.loginUser(loginRequest, authenticationManager, jwtTokenProvider);
    }

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        return authenticateService.getUserInfo();
    }
}
