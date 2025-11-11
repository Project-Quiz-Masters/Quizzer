package com.example.quizzer.quiz; // Fixed package

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * Create a new quiz (simple version)
     */
    public Quiz createQuiz(String title, String description) {
        Quiz quiz = new Quiz(title, description, description, null);
        return quizRepository.save(quiz);
    }
    
    /**
     * Create a new quiz with all fields
     */
    public Quiz addQuiz(String title, String description, String course, Long teacherId) { // Changed to Long
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourse(course);
        quiz.setTeacherId(teacherId); // Now matches Long type
        quiz.setPublished(false);
        quiz.setCreatedAt(LocalDateTime.now());
        return quizRepository.save(quiz);
    }

    /**
     * Get all quizzes
     */
    public List<Quiz> listQuizzes() {
        return quizRepository.findAll();
    }

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
     * Update a quiz with basic fields
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
     * Update a quiz with all fields (for Thymeleaf forms)
     */
    public Quiz updateQuiz(Long id, String title, String description, String course, boolean published) {
        Optional<Quiz> existingQuiz = quizRepository.findById(id);
        
        if (existingQuiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + id + " not found");
        }
        
        Quiz quiz = existingQuiz.get();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourse(course);
        quiz.setPublished(published);
        return quizRepository.save(quiz);
    }

    /**
     * Update a quiz with Quiz object (for REST APIs)
     */
    public Quiz updateQuiz(Long id, Quiz quizDetails) {
        Optional<Quiz> existingQuiz = quizRepository.findById(id);
        
        if (existingQuiz.isEmpty()) {
            return null;
        }
        
        Quiz quiz = existingQuiz.get();
        quiz.setTitle(quizDetails.getTitle());
        quiz.setDescription(quizDetails.getDescription());
        quiz.setCourse(quizDetails.getCourse());
        quiz.setPublished(quizDetails.isPublished());
        // Don't update teacherId for security
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

    /**
     * Delete quiz with teacher authorization
     */
    public boolean deleteQuiz(Long id, Long teacherId) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isPresent() && quiz.get().getTeacherId().equals(teacherId)) {
            quizRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
