package com.example.Library.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UpdateDetailsRequest {

    @Size(min = 3, message = "Username must be at least 3 characters")
    public String username;
    @Size(min = 8, message = "Password must be at least 8 characters")
    public String password;
    @Email(message = "Invalid email")
    public String email;
}
