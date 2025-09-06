package com.example.Bookstore.Controller;

import com.example.Bookstore.RequestBody.AddReviewBody;
import com.example.Bookstore.RequestBody.ViewReviewResponseBody;
import com.example.Bookstore.Service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            List<ViewReviewResponseBody> reviewByID = reviewService.getReviewsByBookId(id);
            logger.info("A user has requested a list of reviews from a book.");
            return ResponseEntity.ok(reviewByID);
        } catch (Exception e) {
            logger.error("Failed to show the list of reviews of a book!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("book/{id}")
    @Operation(summary = "Add a review.")
    public ResponseEntity<String> addReview(@PathVariable Integer id,@Valid @RequestBody AddReviewBody reviewBody) {
        try {
            String addedReview = reviewService.addReview(id,reviewBody);
            logger.info("A review has been created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(addedReview);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create a new review.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
