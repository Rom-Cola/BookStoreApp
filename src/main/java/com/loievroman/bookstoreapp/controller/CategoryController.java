package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.category.CategoryDto;
import com.loievroman.bookstoreapp.dto.category.CreateCategoryRequestDto;
import com.loievroman.bookstoreapp.service.BookService;
import com.loievroman.bookstoreapp.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(
            summary = "Get all categories (list)",
            description = "Returns a list of all available categories."
    )
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully.")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @Operation(
            summary = "Get all categories",
            description = "Returns a paginated list of all available categories."
    )
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully.")
    @GetMapping(params = "page")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(
            summary = "Create new category",
            description = "Creates a new category. Admin access required."
    )
    @ApiResponse(responseCode = "201", description = "Category created successfully.")
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Operation(
            summary = "Get category by ID",
            description = "Returns a category with the specified ID."
    )
    @ApiResponse(responseCode = "200", description = "Category retrieved successfully.")
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update category by ID",
            description = "Updates an existing category. Admin access required."
    )
    @ApiResponse(responseCode = "200", description = "Category updated successfully.")
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(
            summary = "Delete category by ID",
            description = "Deletes the category with the specified ID. Admin access required."
    )
    @ApiResponse(responseCode = "204", description = "Category deleted successfully.")
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(
            summary = "Get books by category ID",
            description = "Returns a list of books that belong to the specified category."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Books retrieved successfully for the category."
    )
    @ApiResponse(responseCode = "404", description = "Category not found.")
    @GetMapping("/{id}/books")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookDto> getBooksByCategoryId(@PathVariable Long id) {
        return bookService.getBooksByCategoryId(id);
    }
}
