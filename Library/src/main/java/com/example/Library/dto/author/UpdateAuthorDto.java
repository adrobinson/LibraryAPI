package com.example.Library.dto.author;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UpdateAuthorDto {
    @Size(min = 5, message = "Name field must be 5 characters or more")
    public String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @JsonProperty("dob")
    @Past(message = "birth-date must be in the past")
    public LocalDate birthDate;
}
