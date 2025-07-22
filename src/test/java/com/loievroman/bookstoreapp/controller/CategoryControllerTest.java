package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.category.CategoryDto;
import com.loievroman.bookstoreapp.dto.category.CreateCategoryRequestDto;
import com.loievroman.bookstoreapp.service.CategoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDto categoryDto;
    private CreateCategoryRequestDto createCategoryRequestDto;

    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");

        createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("Test Category");
        createCategoryRequestDto.setDescription("Test Description");
    }

    @Test
    void getAll_ReturnsListOfCategoryDto() {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(categoryDto);

        when(categoryService.findAll()).thenReturn(categoryDtoList);

        List<CategoryDto> result = categoryController.getAll();

        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));
    }

    @Test
    void getAll_WithPageable_ReturnsPageOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(categoryDto);
        Page<CategoryDto> categoryDtoPage = new PageImpl<>(categoryDtoList, pageable, 1);

        when(categoryService.findAll(pageable)).thenReturn(categoryDtoPage);

        Page<CategoryDto> result = categoryController.getAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(categoryDto, result.getContent().get(0));
    }

    @Test
    void createCategory_CreatesAndReturnsCategoryDto() {
        when(categoryService.createCategory(createCategoryRequestDto)).thenReturn(categoryDto);

        CategoryDto result = categoryController.createCategory(createCategoryRequestDto);

        assertEquals(categoryDto, result);
    }

    @Test
    void getCategoryById_ReturnsCategoryDto() {
        when(categoryService.getById(1L)).thenReturn(categoryDto);

        CategoryDto result = categoryController.getCategoryById(1L);

        assertEquals(categoryDto, result);
    }

    @Test
    void updateCategory_UpdatesAndReturnsCategoryDto() {
        when(categoryService.update(1L, categoryDto)).thenReturn(categoryDto);

        CategoryDto result = categoryController.updateCategory(1L, categoryDto);

        assertEquals(categoryDto, result);
    }

    @Test
    void deleteCategory_DeletesCategoryAndReturnsNoContent() {
        categoryController.deleteCategory(1L);
        verify(categoryService, times(1)).deleteById(1L);
    }
}