package com.example.Library.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDto {
    private Integer id;
    private String username;
    private String bookTitle;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
