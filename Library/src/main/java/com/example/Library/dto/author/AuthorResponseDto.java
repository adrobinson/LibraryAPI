package com.example.Library.dto.author;

import com.example.Library.entity.Book;

import java.time.LocalDate;
import java.util.List;

public class AuthorResponseDto {

    public String name;
    public LocalDate birthDate;
    public List<Book> bookList;

}
