package com.example.Library.controller;

import com.example.Library.dto.book.BookDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> saveBook(@Valid @RequestBody BookDto dto){
        return ResponseEntity.ok(bookService.saveBook(dto));
    }
}
