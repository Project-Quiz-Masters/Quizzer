package com.example.quizzer.repository;

import com.example.quizzer.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * QuizRepository
 * 
 * RELATED TO ALL USER STORIES 1-6:
 * - USER STORY 1: Create quiz
 * - USER STORY 2: View list of quizzes
 * - USER STORY 3: Edit quiz
 * - USER STORY 4: Delete quiz
 * - USER STORY 5: Add questions to quiz
 * - USER STORY 6: View questions in quiz
 * 
 * Data access layer for Quiz entity.
 * Provides CRUD operations for quizzes.
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
