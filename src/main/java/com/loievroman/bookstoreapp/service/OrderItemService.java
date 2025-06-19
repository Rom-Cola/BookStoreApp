package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.orderitem.OrderItemDto;
import com.loievroman.bookstoreapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {

    Page<OrderItemDto> findOrderItems(User user, Long orderId, Pageable pageable);

    OrderItemDto findOrderItem(User user, Long orderId, Long orderItemId);
}
