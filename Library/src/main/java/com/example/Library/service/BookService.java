package com.example.Library.service;

import com.example.Library.dto.book.BookDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.entity.Book;
import com.example.Library.repository.AuthorRepository;
import com.example.Library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookResponseDto saveBook(BookDto dto){
        var author = authorRepository.findByName(dto.author)
                .orElseThrow(() -> new NoSuchElementException("No author of name: " + dto.author));

        var exists = bookRepository.findBookByTitleAndAuthor(dto.title, author);
        if(exists.isPresent()){
            throw new DataIntegrityViolationException("This book already exists");
        }
        Book book = bookMapper.mapToBook(dto, author);
        author.addBook(book);
        bookRepository.save(book);
        return(bookMapper.toBookResponse(book));


    }

}
