package com.example.quizzer.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find all reviews for a quiz
    List<Review> findByQuizId(Long quizId);

    // Count reviews for a quiz
    int countByQuizId(Long quizId);

    // Used to calculate the average ratings for each Quiz
    Double findAverageRatingByQuizId(Long quizId);

    // Used to show the average rating
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.quiz.id = :quizId")
    Double getAverageRating(Long quizId);
}