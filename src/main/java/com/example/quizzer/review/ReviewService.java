package com.example.quizzer.review;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizService;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private QuizService quizService;

    public Review addReview(Long quizId, Review payload) {
        Optional<Quiz> maybeQuiz = quizService.getQuizById(quizId);
        if (maybeQuiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }

        Review review = new Review();
        review.setNickname(payload.getNickname());
        review.setRating(payload.getRating());
        review.setText(payload.getText());
        review.setQuiz(maybeQuiz.get());

        return reviewRepository.save(review);
    }

    // Update an existing review
    public Review updateReview(Long reviewId, Review payload) {
        // Find existing review
        Optional<Review> maybeReview = reviewRepository.findById(reviewId);

        // If review does not exist, return null, controller turns this into 404
        if (maybeReview.isEmpty()) {
            return null;
        }

        // Validation
        if (payload.getNickname() == null || payload.getNickname().trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname is required");
        }

        if (payload.getText() == null || payload.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Review text is required");
        }

        int rating = payload.getRating();
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = maybeReview.get();
        review.setNickname(payload.getNickname());
        review.setRating(rating);
        review.setText(payload.getText());

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForQuizNewestFirst(Long quizId) {
        return reviewRepository.findByQuizId(quizId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Double getAverageRating(Long quizId) {
        // Use JPQL AVG for precision; fallback to 0.0 if null
        Double avg = reviewRepository.getAverageRating(quizId);
        return avg != null ? avg : 0.0;
    }

    public int getReviewCount(Long quizId) {
        return reviewRepository.countByQuizId(quizId);
    }

    public boolean deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }
}
