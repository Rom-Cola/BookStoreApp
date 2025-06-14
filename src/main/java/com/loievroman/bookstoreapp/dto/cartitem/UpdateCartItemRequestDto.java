package com.loievroman.bookstoreapp.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRequestDto {
    @Positive(message = "Quantity must be positive")
    private int quantity;
}
