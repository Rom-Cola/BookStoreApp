package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import com.loievroman.bookstoreapp.service.BookService;
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
import java.math.BigDecimal;
import java.util.HashSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;

    @BeforeEach
    void setUp() {
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("1234567890");
        bookDto.setPrice(BigDecimal.valueOf(20.00));
        bookDto.setDescription("Test Description");
        bookDto.setCoverImage("test.jpg");
        bookDto.setCategoriesIds(new HashSet<>());

        createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Test Book");
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setIsbn("1234567890");
        createBookRequestDto.setPrice(BigDecimal.valueOf(20.00));
        createBookRequestDto.setDescription("Test Description");
        createBookRequestDto.setCoverImage("test.jpg");
        createBookRequestDto.setCategoriesIds(new HashSet<>());
    }

    @Test
    void findAll_ReturnsPageOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<BookDto> bookDtoPage = new PageImpl<>(List.of(bookDto), pageable, 1);

        when(bookService.findAll(pageable)).thenReturn(bookDtoPage);

        Page<BookDto> result = bookController.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(bookDto, result.getContent().get(0));
    }

    @Test
    void findById_ReturnsBookDto() {
        when(bookService.findById(1L)).thenReturn(bookDto);

        BookDto result = bookController.findById(1L);

        assertEquals(bookDto, result);
    }

    @Test
    void createBook_CreatesAndReturnsBookDto() {
        when(bookService.createBook(createBookRequestDto)).thenReturn(bookDto);

        BookDto result = bookController.createBook(createBookRequestDto);

        assertEquals(bookDto, result);
    }

    @Test
    void update_UpdatesAndReturnsBookDto() {
        when(bookService.update(1L, createBookRequestDto)).thenReturn(bookDto);

        BookDto result = bookController.update(createBookRequestDto, 1L);

        assertEquals(bookDto, result);
    }

    @Test
    void delete_DeletesBookAndReturnsNoContent() {
        bookController.delete(1L);
        verify(bookService, times(1)).delete(1L);
    }
}