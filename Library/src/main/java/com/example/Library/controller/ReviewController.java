package com.example.Library.controller;

import com.example.Library.dto.review.ReviewRequestDto;
import com.example.Library.dto.review.ReviewResponseDto;
import com.example.Library.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/review")
    public ResponseEntity<ReviewResponseDto> addReview(@Valid @RequestBody ReviewRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.leaveReview(dto));
    }

    @GetMapping("/review/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDto>> getBookReviews(@PathVariable Integer bookId) {
        return ResponseEntity.ok(reviewService.getReviewsForBook(bookId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/me/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer reviewId) {
        reviewService.deleteUserReview(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}
