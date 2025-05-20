package com.example.Library.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Author {
    @Id
    @GeneratedValue
    private Integer id;
    @NotBlank
    private String name;
    private LocalDate birthDate;
    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private List<Book> books;
}
