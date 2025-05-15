package com.example.practice.service;

import com.example.practice.entity.User;
import com.example.practice.exception.AlreadyExitsException;
import com.example.practice.repository.UserRepository;
import com.example.practice.security.AppUserDetails;
import com.example.practice.web.model.CreateUserByAdminRequest;
import com.example.practice.web.model.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUserByAdmin(CreateUserByAdminRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExitsException("User already exists");
        }
        User user = User.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .isDeleted(false)
                .roles(createUserRequest.getRoles())
                .build();

        userRepository.save(user);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    public void restoreUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(false);
        userRepository.save(user);
    }

    public void updateUser(Long id, CreateUserByAdminRequest updateUserRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(updateUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        user.setRoles(updateUserRequest.getRoles());
        userRepository.save(user);
    }
}
