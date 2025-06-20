package com.loievroman.bookstoreapp.mapper;

import com.loievroman.bookstoreapp.config.MapperConfig;
import com.loievroman.bookstoreapp.dto.orderitem.OrderItemDto;
import com.loievroman.bookstoreapp.model.CartItem;
import com.loievroman.bookstoreapp.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "price", source = "book.price")
    OrderItem toEntity(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem order);
}
