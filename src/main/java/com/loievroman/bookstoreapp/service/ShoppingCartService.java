package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.cartitem.AddCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.cartitem.UpdateCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import com.loievroman.bookstoreapp.model.User;

public interface ShoppingCartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto addBookToShoppingCart(Long userId, AddCartItemRequestDto addCartItemRequestDto);

    ShoppingCartDto updateCartItemQuantity(
            Long userId,
            Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto
    );

    void deleteCartItemById(Long userId, Long cartItemId);

    void createUserShoppingCart(User user);

    void clearShoppingCart(User user);
}
