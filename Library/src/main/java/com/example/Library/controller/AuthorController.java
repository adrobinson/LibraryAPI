package com.example.Library.controller;


import com.example.Library.service.AuthorMapper;
import com.example.Library.service.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;




}
