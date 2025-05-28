package com.example.Library.service.review;

import com.example.Library.dto.review.ReviewResponseDto;
import com.example.Library.entity.Review;
import com.example.Library.service.user.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReviewMapper {

    private final UserMapper userMapper;

    public ReviewResponseDto toDto(Review review) {
        var dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setBookTitle(review.getBook().getTitle());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
