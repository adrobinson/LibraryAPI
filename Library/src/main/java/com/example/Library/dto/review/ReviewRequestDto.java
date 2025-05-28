package com.example.Library.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequestDto {
    private Integer bookId;
    @Min(1)
    @Max(5)
    @NotNull
    private Integer rating;
    private String comment;
}
