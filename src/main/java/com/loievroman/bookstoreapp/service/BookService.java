package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.BookDto;
import com.loievroman.bookstoreapp.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
