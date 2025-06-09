package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import com.loievroman.bookstoreapp.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Get all books",
            description = "Returns a paginated list of all available books."
    )
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully.")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(
            summary = "Get a book by ID",
            description = "Returns a single book by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(
            summary = "Create a new book",
            description = "Saves a new book using the provided request body."
    )
    @ApiResponse(responseCode = "201", description = "Book created successfully.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.createBook(requestDto);
    }

    @Operation(
            summary = "Update an existing book",
            description = "Updates book information based on the given ID and request body."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully."),
            @ApiResponse(responseCode = "404", description = "Book not found."),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BookDto update(@RequestBody @Valid CreateBookRequestDto requestDto,
                          @PathVariable Long id) {
        return bookService.update(id, requestDto);
    }

    @Operation(
            summary = "Delete a book",
            description = "Removes the book with the given ID from the database."
    )
    @ApiResponse(responseCode = "204", description = "Book deleted successfully.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }

}
