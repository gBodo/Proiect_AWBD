package com.example.Bookstore.Repository;

import com.example.Bookstore.Model.Order_item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<Order_item, Long> {
    List<Order_item> findByOrderId(Long orderId);
}