package com.example.Bookstore.Repository;

import com.example.Bookstore.Model.Cart_item;
import com.example.Bookstore.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(Long id);
}