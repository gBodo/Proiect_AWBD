package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.AddBookBody;
import com.example.Bookstore.RequestBody.DeleteBookBody;
import com.example.Bookstore.RequestBody.UpdatePriceBookBody;
import com.example.Bookstore.Service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin/books")
@Tag(name="ADMIN ONLY!")
public class AdminBookController {

    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(AdminBookController.class);

    @Autowired
    public AdminBookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Admin-only endpoint - only users with ADMIN role can add books
    @PostMapping("/add")
    public ResponseEntity<String> addBook(@Valid @RequestBody AddBookBody addBookBody) {
        try {
            String book = bookService.addBook(addBookBody);
            logger.info("ADMIN has added a new book.");
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        } catch (IllegalArgumentException e) {
            logger.error("ADMIN has failed to add a new book.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> DeleteBook(@Valid @RequestBody DeleteBookBody deleteBookBody) {
        try {
            String deleteBook = bookService.deleteBook(deleteBookBody);
            logger.info("ADMIN has deleted a book.");
            return ResponseEntity.ok(deleteBook);
        } catch (IllegalArgumentException e) {
            logger.error("ADMIN has failed to delete a book.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> UpdateBook(@Valid @RequestBody UpdatePriceBookBody updatePriceBookBody) {
        try {
            String updateBook = bookService.updateBookPrice(updatePriceBookBody);
            logger.info("ADMIN has updated a book's price.");
            return ResponseEntity.ok(updateBook);
        } catch (IllegalArgumentException e) {
            logger.error("ADMIN has failed to update a book's price.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
