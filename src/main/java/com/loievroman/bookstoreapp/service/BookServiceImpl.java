package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.BookDto;
import com.loievroman.bookstoreapp.dto.CreateBookRequestDto;
import com.loievroman.bookstoreapp.mapper.BookMapper;
import com.loievroman.bookstoreapp.model.Book;
import com.loievroman.bookstoreapp.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        Book book = bookOptional.orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id=" + id)
        );
        return bookMapper.toDto(book);
    }
}
