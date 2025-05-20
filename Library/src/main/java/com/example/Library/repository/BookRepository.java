package com.example.Library.repository;

import com.example.Library.entity.Author;
import com.example.Library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findBookByTitleAndAuthor(String title, Author author);
}
