package com.loievroman.bookstoreapp.mapper;

import com.loievroman.bookstoreapp.config.MapperConfig;
import com.loievroman.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import com.loievroman.bookstoreapp.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

}
