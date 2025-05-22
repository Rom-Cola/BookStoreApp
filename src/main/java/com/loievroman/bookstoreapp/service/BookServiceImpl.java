package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.book.BookDto;
import com.loievroman.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import com.loievroman.bookstoreapp.dto.book.CreateBookRequestDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.mapper.BookMapper;
import com.loievroman.bookstoreapp.model.Book;
import com.loievroman.bookstoreapp.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public List<BookDto> getBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllByCategoriesId(categoryId)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDtoWithoutCategoryIds findById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        Book book = bookOptional.orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id=" + id)
        );
        return bookMapper.toDtoWithoutCategories(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't get a book by id: "
                                + id + " to update.")
                );
        Book updatedBook = bookMapper.updateEntity(requestDto, foundBook);
        bookRepository.save(updatedBook);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

}
