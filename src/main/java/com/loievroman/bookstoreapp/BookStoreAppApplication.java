package com.loievroman.bookstoreapp;

import com.loievroman.bookstoreapp.model.Book;
import com.loievroman.bookstoreapp.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreAppApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setIsbn("978-3-16-148");
            book.setAuthor("Tomas");
            book.setTitle("BookTitle");
            book.setPrice(new BigDecimal(15));
            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
