package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.cartitem.AddCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.cartitem.UpdateCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto findById(Long id);

    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto addBookToShoppingCart(Long userId, AddCartItemRequestDto addCartItemRequestDto);

    ShoppingCartDto updateCartItemQuantity(
            Long userId,
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto
    );

    void deleteCartItemById(Long userId, Long cartItemId);
}
