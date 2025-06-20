package com.loievroman.bookstoreapp.repository;

import com.loievroman.bookstoreapp.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o "
            + "WHERE o.id = :orderId "
            + "AND o.user.id = :userId")
    Optional<Order> findOrderByIdAndUserId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId
    );
}
