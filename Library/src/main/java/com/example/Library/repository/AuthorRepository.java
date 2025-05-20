package com.example.Library.repository;

import com.example.Library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Optional<Author> findByNameAndBirthDate(String name, LocalDate dob);
    Optional<Author> findByName(String name);

    List<Author> findByNameContainingIgnoreCase(String name);




}
