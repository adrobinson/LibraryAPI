package com.example.Library.dto.book;

import com.example.Library.enums.Genre;

import java.time.LocalDate;

public class BookResponseDto {

    public String title;
    public Genre genre;
    public String authorName;
    public LocalDate published;

}
