package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
