package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci "
            + "WHERE ci.id = :cartItemId "
            + "AND ci.shoppingCart.id = :userId")
    Optional<CartItem> findByIdAndShoppingCartUserId(
            @Param("cartItemId") Long cartItemId,
            @Param("userId") Long userId
    );
}
