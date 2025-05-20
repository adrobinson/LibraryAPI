package com.example.Library.controller;


import com.example.Library.dto.author.AuthorDto;
import com.example.Library.service.AuthorMapper;
import com.example.Library.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/author")
    public ResponseEntity<?> saveAuthor(@Valid @RequestBody AuthorDto dto) {
        var response = authorService.saveAuthor(dto);
        return ResponseEntity.ok(response);
    }




}
