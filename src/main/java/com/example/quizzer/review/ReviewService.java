package com.example.quizzer.review;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public List<Review> getReviewsByQuiz(Long quizId) {
        return reviewRepository.findByQuizId(quizId);
    }

    public Review updateReview(Long reviewId, Review updatedReview) {
        Optional<Review> existing = reviewRepository.findById(reviewId);

        if (existing.isEmpty()) {
            return null;
        }

        Review review = existing.get();
        review.setNickname(updatedReview.getNickname());
        review.setText(updatedReview.getText());
        review.setRating(updatedReview.getRating());

        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("Review with ID " + reviewId + " not found");
        }
        reviewRepository.deleteById(reviewId);
    }

    public int getReviewCount(Long quizId) {
        return reviewRepository.countByQuizId(quizId);
    }

    public Double getAverageRating(Long quizId) {
        return reviewRepository.getAverageRating(quizId);
    }
}
