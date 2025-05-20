package com.example.Library.entity;
import com.example.Library.enums.Genre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Book {

    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String title;
    @NotNull
    private LocalDate published;
    @NotNull
    private Genre genre;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Review> reviews;

}
