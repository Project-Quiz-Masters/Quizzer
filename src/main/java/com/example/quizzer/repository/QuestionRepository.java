package com.example.quizzer.repository;

import com.example.quizzer.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * QuestionRepository
 * 
 * USER STORY 5: Add question to a quiz
 * USER STORY 6: View list of quiz's questions
 * TASK #13: Implement Question repository
 * 
 * Data access layer for Question entity.
 * Provides CRUD operations and custom queries for questions.
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    /**
     * Find all questions for a specific quiz
     * 
     * Used by:
     * - USER STORY 5, TASK #14: Get questions when managing a quiz
     * - USER STORY 6: Display all questions in a quiz
     */
    List<Question> findByQuizId(Long quizId);
}
