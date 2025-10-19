package com.vdmytriv.patmap.service;

import com.vdmytriv.patmap.dto.review.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto create(ReviewDto dto);

    List<ReviewDto> getAll();

    ReviewDto getById(Long id);

    ReviewDto update(Long id, ReviewDto dto);

    void delete(Long id);
}
