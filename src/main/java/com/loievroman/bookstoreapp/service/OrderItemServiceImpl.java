package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.orderitem.OrderItemDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.mapper.OrderItemMapper;
import com.loievroman.bookstoreapp.model.OrderItem;
import com.loievroman.bookstoreapp.model.User;
import com.loievroman.bookstoreapp.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Page<OrderItemDto> findOrderItems(User user, Long orderId, Pageable pageable) {
        Page<OrderItem> orderItems =
                orderItemRepository.findAllByOrderIdAndUserId(orderId, user.getId(), pageable);
        return orderItems.map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto findOrderItem(User user, Long orderId, Long orderItemId) {
        OrderItem orderItem =
                orderItemRepository.findByOrderIdAndUserIdAndId(orderId, user.getId(), orderItemId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Order item not found for user with id: " + user.getId()
                                        + " order id: " + orderId
                                        + " and order item id: " + orderItemId)
                        );
        return orderItemMapper.toDto(orderItem);
    }
}
