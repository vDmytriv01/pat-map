package com.vdmytriv.patmap.controller;

import com.vdmytriv.patmap.dto.review.CreateReviewRequestDto;
import com.vdmytriv.patmap.dto.review.ReviewDto;
import com.vdmytriv.patmap.service.ReviewService;
import com.vdmytriv.patmap.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places/{placeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long placeId) {
        List<ReviewDto> reviews = reviewService.getByPlaceId(placeId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewDto> addReview(@PathVariable Long placeId,
                                               Authentication authentication,
                                               @Valid @RequestBody CreateReviewRequestDto requestDto) {
        Long userId = userService.getByEmail(authentication.getName()).getId();
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setPlaceId(placeId);
        reviewDto.setUserId(userId);
        reviewDto.setRating(requestDto.getRating());
        reviewDto.setComment(requestDto.getComment());
        ReviewDto created = reviewService.create(reviewDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
