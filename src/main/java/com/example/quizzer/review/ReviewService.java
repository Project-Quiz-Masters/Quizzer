package com.example.quizzer.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired

    public Review addReviewToQuiz(Long quizId, Review payload) {
        throw new UnsupportedOperationException("Unimplemented method 'addReviewToQuiz'");
    }
}
