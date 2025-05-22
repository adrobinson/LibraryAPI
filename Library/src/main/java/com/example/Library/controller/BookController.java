package com.example.Library.controller;

import com.example.Library.dto.PaginatedResponse;
import com.example.Library.dto.book.BookDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.service.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    // Admin Endpoints

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/book")
    public ResponseEntity<BookResponseDto> saveBook(@Valid @RequestBody BookDto dto){
        return ResponseEntity.ok(bookService.saveBook(dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/book/{book-id}")
    public ResponseEntity<BookResponseDto> deleteBook(@PathVariable("book-id") Integer id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    // User Endpoints

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDto>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/books/search/{book-id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable("book-id") Integer id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/books/search")
    public ResponseEntity<List<BookResponseDto>> getBookByName(@RequestParam String name){
        return ResponseEntity.ok(bookService.getBookByName(name));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/books/page")
    public ResponseEntity<PaginatedResponse<BookResponseDto>> getBooksPage(@RequestParam int page,
                                                                           @RequestParam int size){
        return ResponseEntity.ok(bookService.getBooksPage(page,size));
    }
}
