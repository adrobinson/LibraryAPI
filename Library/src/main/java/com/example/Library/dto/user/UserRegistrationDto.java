package com.example.Library.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, message = "Username must be at least 3 characters")
    public String username;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    public String password;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email")
    public String email;
}
