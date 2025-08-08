package com.loievroman.bookstoreapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Find all categories - should return a list of DTOs")
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
    @DisplayName("Find all categories paginated - should return a page of DTOs")
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
    @DisplayName("Get category by existing ID - should return DTO")
    void getById_WithExistingId_ReturnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertEquals(categoryDto, result);
    }

    @Test
    @DisplayName("Get category by non-existing ID - should throw exception")
    void getById_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(1L));
    }

    @Test
    @DisplayName("Create a new category - should create and return DTO")
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
    @DisplayName("Update category with existing ID - should update and return DTO")
    void update_WithExistingId_UpdatesAndReturnsCategoryDto() {
        Long categoryId = 1L;

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        CategoryDto updateRequestDto = new CategoryDto();
        updateRequestDto.setName("New Name");
        updateRequestDto.setDescription("New Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateRequestDto.getName());
        updatedCategory.setDescription(updateRequestDto.getDescription());

        CategoryDto expectedResponseDto = new CategoryDto();
        expectedResponseDto.setId(categoryId);
        expectedResponseDto.setName(updateRequestDto.getName());
        expectedResponseDto.setDescription(updateRequestDto.getDescription());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedResponseDto);

        CategoryDto actualResponseDto = categoryService.update(categoryId, updateRequestDto);

        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto, actualResponseDto);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Update category with non-existing ID - should throw exception")
    void update_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(1L, categoryDto));
    }

    @Test
    @DisplayName("Delete category by ID - should call delete method")
    void deleteById_DeletesCategoryById() {
        categoryService.deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
