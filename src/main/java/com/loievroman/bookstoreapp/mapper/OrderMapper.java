package com.loievroman.bookstoreapp.mapper;

import com.loievroman.bookstoreapp.config.MapperConfig;
import com.loievroman.bookstoreapp.dto.order.CreateOrderRequestDto;
import com.loievroman.bookstoreapp.dto.order.OrderDto;
import com.loievroman.bookstoreapp.model.Order;
import com.loievroman.bookstoreapp.model.OrderItem;
import com.loievroman.bookstoreapp.model.ShoppingCart;
import com.loievroman.bookstoreapp.model.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        config = MapperConfig.class,
        uses = {CartItemMapper.class, OrderItemMapper.class}
)
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "shippingAddress", source = "createOrderRequestDto.shippingAddress"),
            @Mapping(target = "orderItems", source = "shoppingCart.cartItems"),
            @Mapping(
                    target = "status",
                    expression = "java(com.loievroman.bookstoreapp.model.Status.PENDING)"
            ),
            @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    })
    Order toEntity(
            ShoppingCart shoppingCart,
            CreateOrderRequestDto createOrderRequestDto,
            User user
    );

    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    default Order configureOrderItems(Order order, ShoppingCart shoppingCart) {
        if (shoppingCart != null && shoppingCart.getCartItems() != null) {
            Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                    .map(cartItem -> {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setBook(cartItem.getBook());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setPrice(cartItem.getBook().getPrice());
                        orderItem.setOrder(order);
                        return orderItem;
                    })
                    .collect(Collectors.toSet());
            order.setOrderItems(orderItems);
        }
        return order;
    }
}
