package com.example.Library.service.review;

import com.example.Library.dto.review.ReviewRequestDto;
import com.example.Library.dto.review.ReviewResponseDto;
import com.example.Library.entity.Book;
import com.example.Library.entity.Review;
import com.example.Library.entity.User;
import com.example.Library.repository.BookRepository;
import com.example.Library.repository.ReviewRepository;
import com.example.Library.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponseDto leaveReview(ReviewRequestDto dto) {
        User user = userService.getCurrentUser();
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        if (reviewRepository.existsByUserAndBook(user, book)) {
            throw new IllegalArgumentException("You have already reviewed this book");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    public List<ReviewResponseDto> getReviewsForBook(Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        return reviewRepository.findAllByBook(book)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUserReview(Integer reviewId) {
        User currentUser = userService.getCurrentUser();

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));

        if (!review.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }


}
