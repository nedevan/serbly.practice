package com.example.practice.web.controller;

import com.example.practice.entity.User;
import com.example.practice.service.UserService;
import com.example.practice.web.model.LoginRequest;
import com.example.practice.web.model.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app/user")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public User getInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/update")
    public ResponseEntity<SimpleResponse> update(@RequestBody LoginRequest loginRequest) {
        userService.updateUser(loginRequest);
        return ResponseEntity.ok(new SimpleResponse("User updated successfully"));
    }
}
