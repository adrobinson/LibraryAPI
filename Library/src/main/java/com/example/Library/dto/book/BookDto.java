package com.example.Library.dto.book;

import com.example.Library.entity.Author;
import com.example.Library.enums.Genre;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class BookDto {
    @NotNull
    @Size(min = 2, message = "title must be 2 characters or longer")
    public String title;
    @NotNull(message = "publish date must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Past(message = "publish date must be in the past")
    public LocalDate published;
    @NotBlank(message = "book must have a genre")
    public Genre genre;
    @NotNull(message = "book must have an author")
    private Author author;

}
