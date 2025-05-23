package com.example.Library.service;

import com.example.Library.dto.book.BookRequestDto;
import com.example.Library.dto.book.BookResponseDto;
import com.example.Library.dto.user.UserResponseDto;
import com.example.Library.entity.Book;
import com.example.Library.entity.User;
import com.example.Library.repository.UserRepository;
import com.example.Library.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@AllArgsConstructor
@Service
public class BookListService {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository repository;
    private final BookService bookService;

    public List<BookResponseDto> getBookListOfCurrent(){
        User user = userService.getCurrentUser();
        return userMapper.getUserBookList(user);
    }

    public List<BookResponseDto> addToBookList(BookRequestDto dto){
        User user = userService.getCurrentUser();
        Set<Book> booksToAdd = extractBooksFromDto(dto);

        if(booksToAdd.isEmpty()){
            throw new IllegalArgumentException("No valid field provided, valid fields include:\nbookId : Integer id\nbookIds: [Integer id]\nbookTitle: String title\nbookTitles: [String titles]");
        }

        booksToAdd.forEach(user::addBook);
        repository.save(user);
        return userMapper.getUserBookList(user);

    }

    public List<BookResponseDto> removeFromBookList(BookRequestDto dto){
        User user = userService.getCurrentUser();
        Set<Book> bookList = user.getBooks();
        Set<Book> booksToRemove = extractBooksFromDto(dto);

        if(booksToRemove.isEmpty()){
            throw new IllegalArgumentException("No valid field provided, valid fields include:\nbookId : Integer id\nbookIds: [Integer id]\nbookTitle: String title\nbookTitles: [String titles]");
        }

        booksToRemove.forEach(book -> {removeBook(book, bookList);});
        repository.save(user);
        return userMapper.getUserBookList(user);
    }

    private void removeBook(Book book, Set<Book> bookList){
        if(bookList.contains(book)){
            bookList.remove(book);
        } else {
            throw new NoSuchElementException("Book list does not contain '" + book.getTitle() + "'");
        }
    }

    private Set<Book> extractBooksFromDto(BookRequestDto dto){
        Set<Book> books = new HashSet<>();

        if (dto.bookId != null) {
            books.add(bookService.findBookById(dto.bookId));
        }

        if (dto.bookIds != null && !dto.bookIds.isEmpty()) {
            dto.bookIds.forEach(id -> books.add(bookService.findBookById(id)));
        }

        if (dto.bookTitle != null && !dto.bookTitle.trim().isEmpty()) {
            books.add(bookService.findBookByTitle(StringUtil.normalizeString(dto.bookTitle)));
        }

        if (dto.bookTitles != null && !dto.bookTitles.isEmpty()) {
            dto.bookTitles.forEach(title -> {
                books.add(bookService.findBookByTitle(StringUtil.normalizeString(title)));
            });
        }

        return books;
    }

}
