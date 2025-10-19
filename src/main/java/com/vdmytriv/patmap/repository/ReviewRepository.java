package com.vdmytriv.patmap.repository;

import com.vdmytriv.patmap.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
