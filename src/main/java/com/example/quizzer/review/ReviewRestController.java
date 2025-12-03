package com.example.quizzer.review;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Reviews", description = "Operations for managing quiz reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

        @PostMapping("/quizzes/{quizId}/reviews")
    @Operation(summary = "Add a review", description = "Adds a new review to the specified quiz.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid review data")
    })
    public ResponseEntity<Review> addReview(@PathVariable Long quizId, @RequestBody Review payload) {

        // Add review
        Review created = reviewService.addReview(quizId, payload);

        // Return created review with location header
        URI location = URI.create(String.format("/api/quizzes/%d/reviews/%d", quizId, created.getId()));
        return ResponseEntity.created(location).body(created);
    }
}
