package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.category.CategoryDto;
import com.loievroman.bookstoreapp.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll();

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
