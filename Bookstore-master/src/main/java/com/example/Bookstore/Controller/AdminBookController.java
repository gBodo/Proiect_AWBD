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

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@Valid @RequestBody AddBookBody addBookBody) {
        logger.info("ADMIN is adding a new book.");
        String book = bookService.addBook(addBookBody);
        logger.info("ADMIN successfully added a new book: {}", book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBook(@Valid @RequestBody DeleteBookBody deleteBookBody) {
        logger.info("ADMIN is deleting a book.");
        String deleted = bookService.deleteBook(deleteBookBody);
        logger.info("ADMIN successfully deleted a book: {}", deleted);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBook(@Valid @RequestBody UpdatePriceBookBody updatePriceBookBody) {
        logger.info("ADMIN is updating a book's price.");
        String updated = bookService.updateBookPrice(updatePriceBookBody);
        logger.info("ADMIN successfully updated a book: {}", updated);
        return ResponseEntity.ok(updated);
    }
}
