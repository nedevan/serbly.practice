package com.example.practice.web.controller;

import com.example.practice.security.SecurityService;
import com.example.practice.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(("/api/auth"))
@RequiredArgsConstructor
public class AuthController {
    private final SecurityService securityService;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<SimpleResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        securityService.createUser(createUserRequest);
        return ResponseEntity.ok(new SimpleResponse("User created"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRequest));
    }
}
