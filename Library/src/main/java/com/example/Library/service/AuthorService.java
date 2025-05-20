package com.example.Library.service;

import com.example.Library.dto.author.AuthorDto;
import com.example.Library.dto.author.AuthorResponseDto;
import com.example.Library.entity.Author;
import com.example.Library.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthorService {

    AuthorMapper authorMapper;
    AuthorRepository repository;

    public AuthorResponseDto saveAuthor(AuthorDto dto){
        Author author = authorMapper.mapDtoToAuthor(dto);
        repository.save(author);
        return authorMapper.mapAuthorToResponse(author);

    }
}
