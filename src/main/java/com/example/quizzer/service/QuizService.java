package com.example.quizzer.service;

import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * Create a new quiz
     */
    public Quiz createQuiz(String title, String description) {
        Quiz quiz = new Quiz(title, description);
        return quizRepository.save(quiz);
    }
    
    /**
     * Get all quizzes
     */
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
    
    /**
     * Get a quiz by ID
     */
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }
    
    /**
     * Update a quiz
     */
    public Quiz updateQuiz(Long id, String title, String description) {
        Optional<Quiz> existingQuiz = quizRepository.findById(id);
        
        if (existingQuiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + id + " not found");
        }
        
        Quiz quiz = existingQuiz.get();
        quiz.setTitle(title);
        quiz.setDescription(description);
        
        return quizRepository.save(quiz);
    }
    
    /**
     * Delete a quiz
     */
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new IllegalArgumentException("Quiz with ID " + id + " not found");
        }
        quizRepository.deleteById(id);
    }
}
