package com.example.Library.controller;

import com.example.Library.dto.review.ReviewRequestDto;
import com.example.Library.dto.review.ReviewResponseDto;
import com.example.Library.service.review.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/review")
    public ResponseEntity<ReviewResponseDto> addReview(@RequestBody ReviewRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.leaveReview(dto));
    }
}
