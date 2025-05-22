package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.Book;
import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = "categories")
    List<Book> findAllByCategoriesId(Long categoryId);

    @Nonnull
    @EntityGraph(attributePaths = "categories")
    List<Book> findAll();

    @Nonnull
    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(@Nonnull Long id);
}
