package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi "
            + "WHERE oi.order.id = :orderId "
            + "AND oi.order.user.id = :userId"
    )
    Page<OrderItem> findAllByOrderIdAndUserId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT oi FROM OrderItem oi "
            + "WHERE oi.order.id = :orderId "
            + "AND oi.order.user.id = :userId "
            + "AND oi.id = :orderItemId"
    )
    Optional<OrderItem> findByOrderIdAndUserIdAndId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId,
            @Param("orderItemId") Long orderItemId
    );
}
