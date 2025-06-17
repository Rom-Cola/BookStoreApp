package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.cartitem.AddCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.cartitem.UpdateCartItemRequestDto;
import com.loievroman.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import com.loievroman.bookstoreapp.model.User;
import com.loievroman.bookstoreapp.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Get current user's shopping cart",
            description = "Returns the shopping cart of the currently authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Shopping cart retrieved successfully.")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto getUsersShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.findByUserId(user.getId());
    }

    @Operation(
            summary = "Add a book to shopping cart",
            description = "Adds a new cart item "
                    + "(book + quantity) to the authenticated user's shopping cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book added to cart successfully."),
            @ApiResponse(responseCode = "404", description = "Invalid input. "
                    + "Shopping cart or Book not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto addBookToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid AddCartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToShoppingCart(user.getId(), requestDto);
    }

    @Operation(
            summary = "Update quantity of a cart item",
            description = "Updates the quantity of a specific "
                    + "cart item in the authenticated user's shopping cart."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully."),
            @ApiResponse(responseCode = "404", description = "Cart item not found.")
    })
    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ShoppingCartDto updateCartItem(
            Authentication authentication,
            @RequestBody UpdateCartItemRequestDto requestDto,
            @PathVariable Long id
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItemQuantity(user.getId(), id, requestDto);
    }

    @Operation(
            summary = "Delete a cart item",
            description = "Removes a cart item from the authenticated user's shopping cart by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart item deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Cart item not found."),
    })
    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteCartItem(
            Authentication authentication,
            @PathVariable Long id
    ) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItemById(user.getId(), id);
    }
}
