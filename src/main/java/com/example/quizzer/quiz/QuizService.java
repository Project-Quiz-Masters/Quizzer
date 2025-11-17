package com.example.quizzer.quiz;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private com.example.quizzer.category.CategoryService categoryService;
    
    /**
     * Create a new quiz (simple version)
     */
    public Quiz createQuiz(String title, String description) {
        // FIXED: Use proper parameters - title, description, course, teacherId
        Quiz quiz = new Quiz(title, description, "Default Course", 1L); // Provide default values
        return quizRepository.save(quiz);
    }
    
    /**
     * Create a new quiz with all fields
     */
    public Quiz addQuiz(String title, String description, String course, Long teacherId) {
        // FIXED: Use the constructor properly
        Quiz quiz = new Quiz(title, description, course, teacherId);
        return quizRepository.save(quiz);
    }

    /**
     * Add quiz with optional category id
     */
    public Quiz addQuiz(String title, String description, String course, Long teacherId, Long categoryId) {
        Quiz quiz = new Quiz(title, description, course, teacherId);
        if (categoryId != null) {
            categoryService.getCategoryById(categoryId).ifPresent(quiz::setCategory);
        }
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
    public Optional<Quiz> getQuizById(Long quizId) {
        return quizRepository.findById(quizId);
    }

    /**
     * Update a quiz with all fields (for Thymeleaf forms)
     */
    public Quiz updateQuiz(Long quizId, String title, String description, String course, boolean published) {
        Optional<Quiz> existingQuiz = quizRepository.findById(quizId);
        
        if (existingQuiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }
        
        Quiz quiz = existingQuiz.get();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourse(course);
        quiz.setPublished(published);
        return quizRepository.save(quiz);
    }

    /**
     * Update quiz and optionally its category
     */
    public Quiz updateQuiz(Long quizId, String title, String description, String course, boolean published, Long categoryId) {
        Optional<Quiz> existingQuiz = quizRepository.findById(quizId);
        
        if (existingQuiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }
        
        Quiz quiz = existingQuiz.get();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourse(course);
        quiz.setPublished(published);
        if (categoryId != null) {
            categoryService.getCategoryById(categoryId).ifPresent(quiz::setCategory);
        } else {
            quiz.setCategory(null);
        }
        return quizRepository.save(quiz);
    }

    /**
     * Update a quiz with Quiz object (for REST APIs)
     */
    public Quiz updateQuiz(Long quizId, Quiz quizDetails) {
        Optional<Quiz> existingQuiz = quizRepository.findById(quizId);
        
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
     * Assign a category to a quiz. Returns the updated quiz or null if quiz or category not found.
     */
    public Quiz assignCategory(Long quizId, Long categoryId) {
        Optional<Quiz> existingQuiz = quizRepository.findById(quizId);
        if (existingQuiz.isEmpty()) {
            return null;
        }

        Quiz quiz = existingQuiz.get();
        if (categoryId == null) {
            // clear category
            quiz.setCategory(null);
            return quizRepository.save(quiz);
        }

        var maybeCat = categoryService.getCategoryById(categoryId);
        if (maybeCat.isEmpty()) {
            return null;
        }

        quiz.setCategory(maybeCat.get());
        return quizRepository.save(quiz);
    }

    /**
     * Assign a category to a quiz with explicit checks.
     * Throws IllegalArgumentException if the quiz or category does not exist.
     */
    public Quiz assignCategoryToQuiz(Long quizId, Long categoryId) {
        Optional<Quiz> existingQuiz = quizRepository.findById(quizId);
        if (existingQuiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }

        Quiz quiz = existingQuiz.get();
        if (categoryId == null) {
            quiz.setCategory(null);
            return quizRepository.save(quiz);
        }

        var maybeCat = categoryService.getCategoryById(categoryId);
        if (maybeCat.isEmpty()) {
            throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
        }

        quiz.setCategory(maybeCat.get());
        return quizRepository.save(quiz);
    }
    
    /**
     * Delete a quiz
     */
    public void deleteQuiz(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }
        quizRepository.deleteById(quizId);
    }

    /**
     * Delete quiz with teacher authorization
     */
    public boolean deleteQuiz(Long quizId, Long teacherId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if (quiz.isPresent() && quiz.get().getTeacherId().equals(teacherId)) {
            quizRepository.deleteById(quizId);
            return true;
        }
        return false;
    }
}