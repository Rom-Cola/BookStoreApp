package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto createBook(CreateBookRequestDto book);

    Page<BookDto> findAll(Pageable pageable);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Pageable pageable, Long categoryId);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    boolean existsByIsbn(String isbn);
}
