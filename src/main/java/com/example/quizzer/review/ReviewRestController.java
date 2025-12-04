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

    @GetMapping("/quizzes/{quizId}/reviews")
    @Operation(summary = "List reviews for a quiz", description = "Returns reviews newest-first with summary stats.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<ReviewsResponse> getReviews(@PathVariable Long quizId) {
        // Note: We allow empty reviews and do not 404 if quiz has no reviews.
        // If strict quiz existence check is required, wire QuizService and verify.

        var reviews = reviewService.getReviewsForQuizNewestFirst(quizId);
        var avg = reviewService.getAverageRating(quizId);
        var count = reviewService.getReviewCount(quizId);

        ReviewsResponse payload = new ReviewsResponse(reviews, avg, count);
        return ResponseEntity.ok(payload);
    }

    public static class ReviewsResponse {
        private final java.util.List<Review> reviews;
        private final Double averageRating;
        private final Integer count;

        public ReviewsResponse(java.util.List<Review> reviews, Double averageRating, Integer count) {
            this.reviews = reviews;
            this.averageRating = averageRating;
            this.count = count;
        }

        public java.util.List<Review> getReviews() { return reviews; }
        public Double getAverageRating() { return averageRating; }
        public Integer getCount() { return count; }
    }
}
