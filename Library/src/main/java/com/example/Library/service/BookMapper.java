package com.example.Library.service;

import com.example.Library.dto.book.BookDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.entity.Author;
import com.example.Library.entity.Book;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book mapToBook(BookDto dto, Author author){
        Book book = new Book();
        book.setTitle((dto.title).toLowerCase().trim());
        book.setGenre(dto.genre);
        book.setAuthor(author);
        book.setPublished(dto.published);
        return book;
    }

    public BookResponseDto toBookResponse(Book book) {
        BookResponseDto response = new BookResponseDto();
        response.title = book.getTitle();
        response.genre = book.getGenre();
        response.authorName = book.getAuthor().getName();
        response.published = book.getPublished();
        return response;
    }
}
