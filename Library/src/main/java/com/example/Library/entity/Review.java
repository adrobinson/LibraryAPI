package com.example.Library.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(
        name = "review",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "book_id"}) // user can only review book once
        }
)
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
    @JsonBackReference
    private Book book;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    @JsonBackReference
    private User user;
}
