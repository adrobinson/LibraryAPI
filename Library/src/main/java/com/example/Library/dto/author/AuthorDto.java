package com.example.Library.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AuthorDto {
    @NotBlank
    @NotNull
    @Size(min = 5, message = "Name field must be 5 characters or more")
    public String name;
    @NotBlank
    @NotNull(message = "Date of birth is required")
    public LocalDate birthDate;
}
