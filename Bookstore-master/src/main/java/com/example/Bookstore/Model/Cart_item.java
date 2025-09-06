package com.example.Bookstore.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class Cart_item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart; // FK → cart.id

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book; // FK → books.id

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
