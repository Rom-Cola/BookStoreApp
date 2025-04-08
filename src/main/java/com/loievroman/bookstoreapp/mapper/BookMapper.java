package com.loievroman.bookstoreapp.mapper;

import com.loievroman.bookstoreapp.config.MapperConfig;
import com.loievroman.bookstoreapp.dto.BookDto;
import com.loievroman.bookstoreapp.dto.CreateBookRequestDto;
import com.loievroman.bookstoreapp.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    Book updateModel(CreateBookRequestDto requestDto, @MappingTarget Book book);

}
