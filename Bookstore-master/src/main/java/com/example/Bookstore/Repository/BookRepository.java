package com.example.Bookstore.Repository;

import com.example.Bookstore.Model.Book;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByCategoryId(Integer categoryId, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByCategoryIdAndTitleContainingIgnoreCase(Integer categoryId, String title, Pageable pageable);

    Optional<Book> findById(Integer id);
    Optional<Book> findByTitle(String title);

    @Transactional
    Optional<Book> deleteBookByTitle(String title);
}