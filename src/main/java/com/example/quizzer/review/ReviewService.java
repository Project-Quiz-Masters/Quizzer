package com.example.quizzer.review;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
