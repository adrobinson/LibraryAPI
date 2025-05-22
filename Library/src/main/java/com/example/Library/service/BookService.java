package com.example.Library.service;

import com.example.Library.dto.PaginatedResponse;
import com.example.Library.dto.book.BookDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.entity.Book;
import com.example.Library.repository.AuthorRepository;
import com.example.Library.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookService {

    private final BookMapper bookMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookResponseDto saveBook(BookDto dto){
        var author = authorRepository.findByName((dto.author).toLowerCase())
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

    public List<BookResponseDto> getAllBooks(){
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
    }

    public BookResponseDto getBookById(Integer id){
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No book of id: " + id));
        return bookMapper.toBookResponse(book);
    }

    public List<BookResponseDto> getBookByName(String name){
        List<Book> books = bookRepository.findBookByTitleContains(name);
        return books.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());

    }

    public PaginatedResponse<BookResponseDto> getBooksPage(int page, int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<BookResponseDto> bookPage = bookRepository.findAll(pageable)
                .map(bookMapper::toBookResponse);
        return new PaginatedResponse<>(bookPage);
    }

}
