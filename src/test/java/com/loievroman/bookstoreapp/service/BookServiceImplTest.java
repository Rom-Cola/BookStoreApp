package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.mapper.BookMapper;
import com.loievroman.bookstoreapp.model.Book;
import com.loievroman.bookstoreapp.repository.BookRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto createBookRequestDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setPrice(BigDecimal.valueOf(20.00));
        book.setDescription("Test Description");
        book.setCoverImage("test.jpg");
        book.setCategories(new HashSet<>());

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
    void findById_WithExistingId_ReturnsBookDto() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(1L);

        assertEquals(bookDto, result);
    }

    @Test
    void findById_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(1L));
    }

    @Test
    void findAll_ReturnsPageOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = new ArrayList<>();
        books.add(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(bookDto, result.getContent().get(0));
    }

    @Test
    void createBook_CreatesAndReturnsBookDto() {
        Book bookToSave = new Book();
        bookToSave.setTitle("Test Book");
        bookToSave.setAuthor("Test Author");
        bookToSave.setIsbn("1234567890");
        bookToSave.setPrice(BigDecimal.valueOf(20.00));
        bookToSave.setDescription("Test Description");
        bookToSave.setCoverImage("test.jpg");
        bookToSave.setCategories(new HashSet<>());

        when(bookMapper.toEntity(createBookRequestDto)).thenReturn(bookToSave);
        when(bookRepository.save(bookToSave)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.createBook(createBookRequestDto);

        assertEquals(bookDto, result);
    }

    @Test
    void update_WithNonExistingId_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(1L, createBookRequestDto));
    }

    @Test
    void delete_DeletesBookById() {
        bookService.delete(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}