package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
