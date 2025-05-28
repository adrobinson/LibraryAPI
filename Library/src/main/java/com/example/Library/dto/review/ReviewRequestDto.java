package com.example.Library.dto.review;

import lombok.Data;

@Data
public class ReviewRequestDto {
    private Integer bookId;
    private Integer rating;
    private String comment;
}
