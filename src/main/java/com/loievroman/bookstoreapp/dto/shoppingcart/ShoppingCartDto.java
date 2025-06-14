package com.loievroman.bookstoreapp.dto.shoppingcart;

import com.loievroman.bookstoreapp.dto.cartitem.CartItemDto;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
