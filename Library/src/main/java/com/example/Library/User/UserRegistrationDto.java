package com.example.Library.User;

import jakarta.validation.constraints.NotEmpty;

public class UserRegistrationDto {
    @NotEmpty(message = "Username cannot be empty")
    public String username;
    @NotEmpty(message = "Password cannot be empty")
    public String password;
    @NotEmpty(message = "Email cannot be empty")
    public String email;
}
