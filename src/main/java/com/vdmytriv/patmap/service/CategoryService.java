package com.vdmytriv.patmap.service;

import com.vdmytriv.patmap.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto dto);

    List<CategoryDto> getAll();

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto dto);

    void delete(Long id);
}
