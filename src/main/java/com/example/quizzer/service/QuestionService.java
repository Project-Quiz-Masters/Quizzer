package com.example.quizzer.service;

import com.example.quizzer.model.Question;
import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuestionRepository;
import com.example.quizzer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * QuestionService
 * 
 * USER STORY 5: Add question to a quiz
 * USER STORY 6: View list of quiz's questions
 * TASK #14: Connect form to database / Implement service
 * 
 * Business logic layer for question operations.
 * Handles all question-related operations and validations.
 */
@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * USER STORY 5, TASK #14: Create a new question for a quiz
     * 
     * Creates a new question with specified text and difficulty level.
     * The question is automatically linked to the specified quiz.
     * Default difficulty: NORMAL if not specified.
     * 
     * @param text The question text content
     * @param difficulty The difficulty level (EASY, NORMAL, HARD)
     * @param quizId The ID of the quiz to add the question to
     * @return The created Question object
     * @throws IllegalArgumentException if quiz with given ID doesn't exist
     */
    public Question createQuestion(String text, String difficulty, Long quizId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        
        if (quiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }
        
        Question question = new Question();
        question.setText(text);
        question.setDifficulty(com.example.quizzer.model.DifficultyLevel.valueOf(difficulty));
        question.setQuiz(quiz.get());
        
        return questionRepository.save(question);
    }
    
    /**
     * USER STORY 6, TASK #18: Get all questions for a specific quiz
     * 
     * Retrieves all questions belonging to a quiz.
     * Used by the question list view to display questions.
     * 
     * @param quizId The ID of the quiz
     * @return List of Question objects for the quiz
     */
    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
    
    /**
     * Get a question by ID
     * 
     * Used internally to retrieve a specific question for editing or retrieval.
     */
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }
    
    /**
     * Update an existing question
     * 
     * Allows teachers to modify question text and difficulty level.
     */
    public Question updateQuestion(Long id, String text, String difficulty) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        
        if (existingQuestion.isEmpty()) {
            throw new IllegalArgumentException("Question with ID " + id + " not found");
        }
        
        Question question = existingQuestion.get();
        question.setText(text);
        question.setDifficulty(com.example.quizzer.model.DifficultyLevel.valueOf(difficulty));
        
        return questionRepository.save(question);
    }
    
    /**
     * Delete a question
     * 
     * USER STORY 7 (Future): Delete a quiz's question
     * 
     * Removes a question from the system and from its associated quiz.
     */
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new IllegalArgumentException("Question with ID " + id + " not found");
        }
        questionRepository.deleteById(id);
    }
    
    /**
     * Get all questions in the system
     * 
     * Used internally for administrative purposes or reporting.
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
