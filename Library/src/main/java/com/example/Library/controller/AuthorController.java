package com.example.Library.controller;


import com.example.Library.dto.PaginatedResponse;
import com.example.Library.dto.author.AuthorDto;
import com.example.Library.dto.author.AuthorResponseDto;
import com.example.Library.service.AuthorMapper;
import com.example.Library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    // Admin Endpoints

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/author")
    public ResponseEntity<?> saveAuthor(@Valid @RequestBody AuthorDto dto) {
        var response = authorService.saveAuthor(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/author/{author-id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable("author-id") Integer id) {
        var response = authorService.deleteAuthor(id);
        return ResponseEntity.ok().body(response);
    }

    // User Endpoints

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/author/all")
    public ResponseEntity<?> getAllAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/author/search/{author-id}")
    public ResponseEntity<?> getAuthorById(@PathVariable("author-id") Integer id){
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/author/search")
    public ResponseEntity<?> getAuthorByName(@RequestParam String name){
        return ResponseEntity.ok(authorService.searchByName(name));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/author/page")
    public ResponseEntity<PaginatedResponse<AuthorResponseDto>> getAuthorsPage(@RequestParam int page,
                                                                                     @RequestParam int size){
        return ResponseEntity.ok(authorService.getAuthorsPage(page, size));
    }


}
