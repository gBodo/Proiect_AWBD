package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.BookBody;
import com.example.Bookstore.Service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book", description = "Operations related to books.")
public class BookController {

    private final BookService bookService;
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    @Operation(summary = "View all the books.")
    public ResponseEntity<Page<BookBody>> getAllBooks(
            @Valid @RequestParam(required = false) Integer category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "title") String sortBy,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        logger.info("Fetching books: category={}, search='{}', sortBy='{}', page={}", category, search, sortBy, page);
        Page<BookBody> books = bookService.getAllBooks(category, search, sortBy, page);
        logger.info("Fetched {} books for page {}", books.getNumberOfElements(), page + 1);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    @Operation(summary = "View a single book's details.")
    public ResponseEntity<BookBody> getBookById(@PathVariable Integer id) {
        logger.info("Fetching details for book with ID: {}", id);
        BookBody dto = bookService.getBookById(id);
        logger.info("Fetched details for book '{}'", dto.getTitle());
        return ResponseEntity.ok(dto);
    }
}
