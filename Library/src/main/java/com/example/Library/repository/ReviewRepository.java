package com.example.Library.repository;

import com.example.Library.entity.Book;
import com.example.Library.entity.Review;
import com.example.Library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    boolean existsByUserAndBook(User user, Book book);
    List<Review> findAllByBook(Book book);
}
