package com.example.Bookstore.Repository;

import com.example.Bookstore.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}