package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    Page<BookDto> findAll(Pageable pageable);

    List<BookDto> getBooksByCategoryId(Long categoryId);

    BookDtoWithoutCategoryIds findById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    boolean existsByIsbn(String isbn);
}
