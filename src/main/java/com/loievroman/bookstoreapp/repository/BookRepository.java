package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAllByCategoriesId(Pageable pageable, Long categoryId);

    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);
}
