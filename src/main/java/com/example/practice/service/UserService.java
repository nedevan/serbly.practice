package com.example.practice.service;

import com.example.practice.entity.User;
import com.example.practice.repository.UserRepository;
import com.example.practice.security.AppUserDetails;
import com.example.practice.web.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User getUserInfo() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();

            return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        }
        return null;
    }

    public void updateUser(LoginRequest loginRequest) {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setUsername(loginRequest.getUsername());
            user.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
            userRepository.save(user);
        }

    }
}
