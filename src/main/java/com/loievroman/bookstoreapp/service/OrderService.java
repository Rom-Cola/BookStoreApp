package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.order.CreateOrderRequestDto;
import com.loievroman.bookstoreapp.dto.order.OrderDto;
import com.loievroman.bookstoreapp.dto.order.UpdateOrderRequestStatusDto;
import com.loievroman.bookstoreapp.dto.orderitem.OrderItemDto;
import com.loievroman.bookstoreapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderItemDto> findOrderItems(User user, Long orderId, Pageable pageable);

    OrderItemDto findOrderItem(User user, Long orderId, Long orderItemId);

    OrderDto createOrder(CreateOrderRequestDto requestDto, User user);

    Page<OrderDto> findAll(User user, Pageable pageable);

    OrderDto updateOrderStatus(UpdateOrderRequestStatusDto requestDto, Long orderId);

    void deleteOrderById(Long id);
}
