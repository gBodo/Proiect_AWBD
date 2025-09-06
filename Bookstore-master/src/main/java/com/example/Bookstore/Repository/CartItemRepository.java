package com.example.Bookstore.Repository;

import com.example.Bookstore.Model.Cart_item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<Cart_item, Long> {
    List<Cart_item> findByCartId(Long cartId);
}