package com.example.demo.controller;

import com.example.demo.base.ResponseBase;
import com.example.demo.modal.LoginRequest;
import com.example.demo.modal.RegisterRequest;
import com.example.demo.modal.UserInfoResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthenticateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@Tag(name = "Authenticator")
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticateService authenticateService;


    @PostMapping("/register")
    public ResponseEntity<ResponseBase<?>> registerUser(@RequestBody RegisterRequest request) {
        return authenticateService.registerUser(request, "USER");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ResponseBase<?>> registerAdmin(@RequestBody RegisterRequest request) {
        return authenticateService.registerUser(request, "ADMIN");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBase<?>> loginUser(@RequestBody LoginRequest loginRequest) {
        return authenticateService.loginUser(loginRequest, authenticationManager, jwtTokenProvider);
    }

    @GetMapping("/user-info")
    public ResponseEntity<ResponseBase<UserInfoResponse>> getUserInfo() {
        return authenticateService.getUserInfo();
    }
}
