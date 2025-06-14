package com.loievroman.bookstoreapp.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequestDto {
    @NotNull(message = "Book ID cannot be null")
    @Positive(message = "Book ID must be positive")
    private Long bookId;
    @Positive(message = "Quantity must be positive")
    private int quantity;
}
