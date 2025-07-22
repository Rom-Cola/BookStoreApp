package com.loievroman.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.loievroman.bookstoreapp.dto.category.CategoryDto;
import com.loievroman.bookstoreapp.dto.category.CreateCategoryRequestDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.mapper.CategoryMapper;
import com.loievroman.bookstoreapp.model.Category;
import com.loievroman.bookstoreapp.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;
    private CreateCategoryRequestDto createCategoryRequestDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Test Category");
        categoryDto.setDescription("Test Description");

        createCategoryRequestDto = new CreateCategoryRequestDto();
        createCategoryRequestDto.setName("Test Category");
        createCategoryRequestDto.setDescription("Test Description");
    }

    @Test
    void findAll_ReturnsListOfCategoryDto() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals(categoryDto, result.get(0));
    }

    @Test
    void findAll_WithPageable_ReturnsPageOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(categoryDto, result.getContent().get(0));
    }

    @Test
    void getById_WithExistingId_ReturnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertEquals(categoryDto, result);
    }

    @Test
    void getById_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(1L));
    }

    @Test
    void createCategory_CreatesAndReturnsCategoryDto() {
        Category categoryToSave = new Category();
        categoryToSave.setName("Test Category");
        categoryToSave.setDescription("Test Description");

        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(categoryToSave);
        when(categoryRepository.save(categoryToSave)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.createCategory(createCategoryRequestDto);

        assertEquals(categoryDto, result);
    }

    @Test
    void update_WithExistingId_UpdatesAndReturnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, categoryDto);

        assertEquals(categoryDto, result);
    }

    @Test
    void update_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(1L, categoryDto));
    }

    @Test
    void deleteById_DeletesCategoryById() {
        categoryService.deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
