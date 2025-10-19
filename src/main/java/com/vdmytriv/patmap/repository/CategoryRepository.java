package com.vdmytriv.patmap.repository;

import com.vdmytriv.patmap.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
