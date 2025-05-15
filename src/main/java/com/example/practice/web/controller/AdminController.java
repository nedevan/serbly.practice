package com.example.practice.web.controller;

import com.example.practice.entity.User;
import com.example.practice.service.AdminService;
import com.example.practice.web.model.CreateUserByAdminRequest;
import com.example.practice.web.model.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/create")
    public ResponseEntity<SimpleResponse> createUser(@RequestBody CreateUserByAdminRequest createUserRequest) {
        adminService.createUserByAdmin(createUserRequest);
        return ResponseEntity.ok(new SimpleResponse("User created"));
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/get/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return adminService.getUserByUsername(username);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<SimpleResponse> deleteUser(@PathVariable String username) {
        adminService.deleteUserByUsername(username);
        return ResponseEntity.ok(new SimpleResponse("User deleted"));
    }

    @PutMapping("/restore/{username}")
    public ResponseEntity<SimpleResponse> restoreUser(@PathVariable String username) {
        adminService.restoreUserByUsername(username);
        return ResponseEntity.ok(new SimpleResponse("User restored"));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<SimpleResponse> updateUser(@PathVariable Long id, @RequestBody CreateUserByAdminRequest updateUserRequest) {
        adminService.updateUser(id, updateUserRequest);
        return ResponseEntity.ok(new SimpleResponse("User updated"));
    }
}
