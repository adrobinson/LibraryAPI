package com.example.Library.service.author;

import com.example.Library.dto.PaginatedResponse;
import com.example.Library.dto.author.AuthorDto;
import com.example.Library.dto.author.AuthorResponseDto;
import com.example.Library.dto.author.UpdateAuthorDto;
import com.example.Library.entity.Author;
import com.example.Library.exception.CredentialsAlreadyExistException;
import com.example.Library.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AuthorService {

    AuthorMapper authorMapper;
    AuthorRepository repository;

    public AuthorResponseDto saveAuthor(AuthorDto dto){
        var exisiting = repository.findByNameAndBirthDate((dto.name).toLowerCase(), dto.birthDate); // Two authors may have same name, DB does not accept two authors with same name AND birthdate
        if(exisiting.isPresent()){
            throw new DataIntegrityViolationException("This author already exists");
        }
        Author author = authorMapper.mapDtoToAuthor(dto);
        repository.save(author);
        return authorMapper.mapAuthorToResponse(author);

    }

    public AuthorResponseDto deleteAuthor(Integer id){
        var author = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No author belonging to id: " + id));
        repository.delete(author);
        return authorMapper.mapAuthorToResponse(author);
    }

    public List<String> updateAuthor(Integer id, UpdateAuthorDto dto){
        Author author = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No author of id: " + id));
        List<String> messages = new ArrayList<>();
        boolean updated = false;

        if(dto.name != null && !dto.name.isBlank()){
            String normalizedName = dto.name.toLowerCase();
            if(repository.existsAuthorByName(normalizedName)){
                throw new CredentialsAlreadyExistException("Author already exists");
            }
            String oldName = author.getName();
            author.setName(normalizedName);
            messages.add("Author name updated: " + oldName + " -> " + normalizedName);
            updated = true;
        }

        if(dto.birthDate != null){
            LocalDate oldDob = author.getBirthDate();
            author.setBirthDate(dto.birthDate);
            messages.add("Author birth-date updated: " + oldDob + " -> " + author.getBirthDate());
            updated = true;
        }

        if(!updated) {
            throw new IllegalArgumentException("No valid fields provided to update");
        }

        repository.save(author);
        return messages;
    }

    public List<AuthorResponseDto> getAllAuthors() {
        return repository.findAll()
                .stream()
                .map(authorMapper::mapAuthorToResponse)
                .collect(Collectors.toList());
    }

    public AuthorResponseDto getAuthorById(Integer id) {
        var author = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No author belonging to id: " + id));
        return authorMapper.mapAuthorToResponse(author);
    }

    public List<AuthorResponseDto> searchByName(String name){
        List<Author> results = repository.findByNameContainingIgnoreCase((name).toLowerCase());
        return results.stream()
                .map(authorMapper::mapAuthorToResponse)
                .collect(Collectors.toList());
    }

    public PaginatedResponse<AuthorResponseDto> getAuthorsPage(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<AuthorResponseDto> authorPage = repository.findAll(pageable)
                .map(authorMapper::mapAuthorToResponse);
        return new PaginatedResponse<>(authorPage);

    }

}
