package com.example.Library.Review;
import com.example.Library.Book.Book;
import com.example.Library.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Min(1)
    @Max(5)
    private int rating;
    private String comment;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "book_id")
    @NotNull
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
}
