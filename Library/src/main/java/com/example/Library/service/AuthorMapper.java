package com.example.Library.service;

import com.example.Library.dto.author.AuthorDto;
import com.example.Library.dto.author.AuthorResponseDto;
import com.example.Library.entity.Author;
import org.springframework.stereotype.Service;

@Service
public class AuthorMapper {

    public Author mapDtoToAuthor(AuthorDto dto){
        Author author = new Author();
        author.setName(dto.name);
        author.setBirthDate(dto.birthDate);
        return author;
    }

    public AuthorResponseDto mapAuthorToResponse(Author author){
        AuthorResponseDto dto = new AuthorResponseDto();
        dto.name = author.getName();
        dto.birthDate = author.getBirthDate();
        return dto;
    }


}
