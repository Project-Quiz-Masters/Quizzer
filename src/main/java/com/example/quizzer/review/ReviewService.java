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
}
