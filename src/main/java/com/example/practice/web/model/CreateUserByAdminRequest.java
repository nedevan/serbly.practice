package com.example.practice.web.model;

import com.example.practice.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserByAdminRequest {
    private String username;

    private Set<RoleType> roles;

    private String password;
}
