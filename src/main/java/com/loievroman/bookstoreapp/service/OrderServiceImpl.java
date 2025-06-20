package com.loievroman.bookstoreapp.service;

import com.loievroman.bookstoreapp.dto.order.CreateOrderRequestDto;
import com.loievroman.bookstoreapp.dto.order.OrderDto;
import com.loievroman.bookstoreapp.dto.order.UpdateOrderRequestStatusDto;
import com.loievroman.bookstoreapp.dto.orderitem.OrderItemDto;
import com.loievroman.bookstoreapp.exception.EntityNotFoundException;
import com.loievroman.bookstoreapp.exception.OrderCreateException;
import com.loievroman.bookstoreapp.mapper.OrderItemMapper;
import com.loievroman.bookstoreapp.mapper.OrderMapper;
import com.loievroman.bookstoreapp.model.Order;
import com.loievroman.bookstoreapp.model.OrderItem;
import com.loievroman.bookstoreapp.model.ShoppingCart;
import com.loievroman.bookstoreapp.model.User;
import com.loievroman.bookstoreapp.repository.OrderItemRepository;
import com.loievroman.bookstoreapp.repository.OrderRepository;
import com.loievroman.bookstoreapp.repository.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
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

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find shopping cart by user id="
                                        + user.getId())
                );
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderCreateException("Cannot create new order for user "
                    + user.getId()
                    + ": shopping cart is empty.");
        }
        Order createdOrder = orderMapper.toEntity(shoppingCart, requestDto, user);

        createdOrder = orderMapper.configureOrderItems(createdOrder, shoppingCart);
        BigDecimal totalPrice = calculateTotal(createdOrder.getOrderItems());
        createdOrder.setTotal(totalPrice);
        createdOrder.setShippingAddress(requestDto.getShippingAddress());
        orderRepository.save(createdOrder);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(createdOrder);
    }

    @Override
    public Page<OrderDto> findAll(User user, Pageable pageable) {
        Page<Order> allByUserId = orderRepository.findAllByUserId(user.getId(), pageable);
        return allByUserId.map(orderMapper::toDto);
    }

    @Override
    public OrderDto updateOrderStatus(
            UpdateOrderRequestStatusDto requestDto,
            Long orderId
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found for order with id: "
                                + orderId)
                );
        order.setStatus(requestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    private BigDecimal calculateTotal(Set<OrderItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
