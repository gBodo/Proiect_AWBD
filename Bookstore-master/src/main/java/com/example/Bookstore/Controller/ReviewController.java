package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.AddReviewBody;
import com.example.Bookstore.RequestBody.ViewReviewResponseBody;
import com.example.Bookstore.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name="Review")
public class ReviewController {

    private final ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("book/{id}")
    @Operation(summary = "View the list of the reviews from a specific book.")
    public ResponseEntity<List<ViewReviewResponseBody>> getReviewsByBookId(@PathVariable Integer id) {
        logger.info("Fetching reviews for book with ID: {}", id);
        List<ViewReviewResponseBody> reviewByID = reviewService.getReviewsByBookId(id);
        logger.info("Fetched {} reviews for book ID {}", reviewByID.size(), id);
        return ResponseEntity.ok(reviewByID);
    }

    @PostMapping("book/{id}")
    @Operation(summary = "Add a review.")
    public ResponseEntity<String> addReview(@PathVariable Integer id, @Valid @RequestBody AddReviewBody reviewBody) {
        logger.info("Adding a review for book ID: {}", id);
        String addedReview = reviewService.addReview(id, reviewBody);
        logger.info("Review added successfully for book ID: {}", id);
        return ResponseEntity.status(201).body(addedReview);
    }
}
