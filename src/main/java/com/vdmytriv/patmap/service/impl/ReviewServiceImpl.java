package com.vdmytriv.patmap.service.impl;

import com.vdmytriv.patmap.dto.review.ReviewDto;
import com.vdmytriv.patmap.mapper.ReviewMapper;
import com.vdmytriv.patmap.model.Place;
import com.vdmytriv.patmap.model.Review;
import com.vdmytriv.patmap.model.User;
import com.vdmytriv.patmap.repository.PlaceRepository;
import com.vdmytriv.patmap.repository.ReviewRepository;
import com.vdmytriv.patmap.repository.UserRepository;
import com.vdmytriv.patmap.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewDto create(ReviewDto dto) {
        Review review = reviewMapper.toEntity(dto);
        review.setUser(getUser(dto.getUserId()));
        review.setPlace(getPlace(dto.getPlaceId()));
        Review saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByPlaceId(Long placeId) {
        return reviewRepository.findAllByPlaceId(placeId).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> getByUserId(Long userId) {
        return reviewRepository.findAllByUserId(userId).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDto getById(Long id) {
        Review review = getReview(id);
        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional
    public ReviewDto update(Long id, ReviewDto dto) {
        Review review = getReview(id);
        reviewMapper.updateReviewFromDto(dto, review);
        if (dto.getUserId() != null
                && (review.getUser() == null || !dto.getUserId().equals(review.getUser().getId()))) {
            review.setUser(getUser(dto.getUserId()));
        }
        if (dto.getPlaceId() != null
                && (review.getPlace() == null || !dto.getPlaceId().equals(review.getPlace().getId()))) {
            review.setPlace(getPlace(dto.getPlaceId()));
        }
        Review updated = reviewRepository.save(review);
        return reviewMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Review review = getReview(id);
        reviewRepository.delete(review);
    }

    private Review getReview(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    private Place getPlace(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));
    }
}
