package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
