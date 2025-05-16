package com.example.Library.dto;

import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDto {
    @NotBlank(message = "Username cannot be empty")
    public String username;
    @NotBlank(message = "Password cannot be empty")
    public String password;
    @NotBlank(message = "Email cannot be empty")
    public String email;
}
