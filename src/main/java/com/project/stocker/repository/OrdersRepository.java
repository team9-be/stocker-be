package com.project.stocker.repository;

import com.project.stocker.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByBuyerIsNotNullAndSellerIsNull();

    List<Orders> findAllBySellerIsNotNullAndBuyerIsNull();
}
