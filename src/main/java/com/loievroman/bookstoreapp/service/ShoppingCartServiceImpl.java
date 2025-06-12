package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.cartitem.AddCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.cartitem.UpdateCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.exception.SecurityException;
import com.loievroman.bookstoreapp.mapper.ShoppingCartMapper;
import com.loievroman.bookstoreapp.model.Book;
import com.loievroman.bookstoreapp.model.CartItem;
import com.loievroman.bookstoreapp.model.ShoppingCart;
import com.loievroman.bookstoreapp.repository.BookRepository;
import com.loievroman.bookstoreapp.repository.CartItemRepository;
import com.loievroman.bookstoreapp.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDto findById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find shopping cart by id=" + id)
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto findByUserId(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find shopping cart by user id=" + userId)
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToShoppingCart(Long userId,
                                                 AddCartItemRequestDto addCartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + userId));

        Book book = bookRepository.findById(addCartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + addCartItemRequestDto.getBookId()));

        CartItem newCartItem = new CartItem();
        newCartItem.setShoppingCart(shoppingCart);
        newCartItem.setBook(book);
        newCartItem.setQuantity(addCartItemRequestDto.getQuantity());

        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.add(newCartItem);
        shoppingCart.setCartItems(cartItems);

        cartItemRepository.save(newCartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItemQuantity(
            Long userId, Long cartItemId,
            UpdateCartItemRequestDto updateCartItemRequestDto
    ) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id: " + cartItemId
                ));

        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new SecurityException("Cart item does not belong to the current user.");
        }
        cartItem.setQuantity(updateCartItemRequestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    @Transactional
    public void deleteCartItemById(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with id: " + cartItemId
                ));

        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new SecurityException("Cart item does not belong to the current user.");
        }

        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.deleteById(cartItemId);
    }
}
