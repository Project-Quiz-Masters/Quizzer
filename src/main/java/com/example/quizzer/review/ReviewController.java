package com.example.quizzer.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.quizzer.quiz.Quiz;

import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Add new review
    @PostMapping("/quiz/{quizId}/add")
    public String addReview(@PathVariable Long quizId,
            @ModelAttribute("newReview") Review review) {

        review.setQuiz(new Quiz());
        review.getQuiz().setId(quizId);

        reviewService.addReview(quizId, review);
        return "redirect:/reviews/quiz/" + quizId;
    }
}
