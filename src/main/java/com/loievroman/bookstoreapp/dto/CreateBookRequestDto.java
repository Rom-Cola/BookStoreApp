package com.loievroman.bookstoreapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {

    @NotNull
    private String title;

    @NotNull
    private String author;

    @Pattern(regexp = "^(97(8|9))?\\d{9}(\\d|X)$", message = "Invalid ISBN format")
    @NotNull
    private String isbn;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    private String description;

    @NotNull
    private String coverImage;
}
