package com.restaurant.dto;

import com.restaurant.model.User.UserRole;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private UserRole role;
}
