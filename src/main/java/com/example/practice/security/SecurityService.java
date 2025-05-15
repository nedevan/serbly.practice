package com.example.practice.security;

import com.example.practice.entity.RefreshToken;
import com.example.practice.entity.RoleType;
import com.example.practice.entity.User;
import com.example.practice.exception.AlreadyExitsException;
import com.example.practice.exception.RefreshTokenException;
import com.example.practice.repository.UserRepository;
import com.example.practice.security.jwt.JwtUtils;
import com.example.practice.service.RefreshTokenService;
import com.example.practice.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        if (userRepository.findByUsername(loginRequest.getUsername()).orElseThrow().isDeleted()) {
            throw new RuntimeException("User not found");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .roles(roles)
                .isDeleted(userDetails.isDeleted())
                .build();
    }

    public void createUser(CreateUserRequest createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExitsException("Username already exists");
        }

        User user = User.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .isDeleted(false)
                .build();
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));

        userRepository.save(user);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Exception trying to get token for userId " + userId));
                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getUsername());

                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token not found"));
    }
}
