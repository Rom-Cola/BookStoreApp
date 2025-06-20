package com.loievroman.bookstoreapp.controller;

import com.loievroman.bookstoreapp.dto.order.CreateOrderRequestDto;
import com.loievroman.bookstoreapp.dto.order.OrderDto;
import com.loievroman.bookstoreapp.dto.order.UpdateOrderRequestStatusDto;
import com.loievroman.bookstoreapp.dto.orderitem.OrderItemDto;
import com.loievroman.bookstoreapp.model.User;
import com.loievroman.bookstoreapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Place an order",
            description = "Creates a new order for the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order created successfully."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Cannot create new order for user, shopping cart is empty"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Can't find shopping cart by user id"
            )
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(requestDto, user);
    }

    @Operation(
            summary = "Retrieve user's order history",
            description = "Retrieves the order history for the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order history retrieved successfully."
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<OrderDto> findAll(
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user, pageable);
    }

    @Operation(
            summary = "Update order status",
            description = "Updates the status of an existing order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order status updated successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found."
                    )
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDto updateOrderStatus(
            Authentication authentication,
            @RequestBody @Valid UpdateOrderRequestStatusDto requestDto,
            @PathVariable Long id
    ) {
        return orderService.updateOrderStatus(requestDto, id);
    }

    @Operation(
            summary = "Retrieve all order items for a specific order",
            description = "Retrieves all order items for a specific order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order items retrieved successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found."
                    )
            }
    )
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<OrderItemDto> findOrderItems(
            Authentication authentication,
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItems(user, orderId, pageable);
    }

    @Operation(
            summary = "Retrieve a specific order item within an order",
            description = "Retrieves a specific order item within an order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order item retrieved successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order or Order item not found."
                    )
            }
    )
    @GetMapping("{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderItemDto findOrderItem(
            Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItem(user, orderId, itemId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
    }
}
