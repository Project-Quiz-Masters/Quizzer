package com.example.quizzer.controller;

import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * QuizController
 * 
 * RELATED TO ALL USER STORIES 1-6:
 * - USER STORY 1: Create quiz
 * - USER STORY 2: View list of quizzes  
 * - USER STORY 3: Edit quiz
 * - USER STORY 4: Delete quiz
 * - USER STORY 5: Add questions to quiz
 * - USER STORY 6: View questions in quiz
 * 
 * TASK #19: Add "View questions" link to quiz list
 * 
 * Handles HTTP requests for quiz management operations.
 * Routes:
 * - GET  /quizzes              -> Show list of all quizzes
 * - GET  /quizzes/new          -> Show create quiz form
 * - POST /quizzes              -> Create new quiz
 * - GET  /quizzes/{id}         -> Show quiz details
 * - POST /quizzes/{id}/delete  -> Delete quiz
 */
@Controller
@RequestMapping("/quizzes")
public class QuizController {
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * USER STORY 1: Display the form to create a new quiz
     * 
     * Route: GET /quizzes/new
     * Template: create-quiz.html
     */
    @GetMapping("/new")
    public String showCreateQuizForm() {
        return "create-quiz";
    }
    
    /**
     * USER STORY 1: Handle form submission to create a new quiz
     * 
     * Route: POST /quizzes
     * Accepts: title, description (optional)
     * Redirect: /quizzes/{id}
     * 
     * Creates a new quiz and saves it to the database.
     */
    @PostMapping
    public String createQuiz(@RequestParam String title,
                            @RequestParam(required = false) String description,
                            Model model) {
        try {
            Quiz quiz = new Quiz(title, description);
            Quiz savedQuiz = quizRepository.save(quiz);
            return "redirect:/quizzes/" + savedQuiz.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Error creating quiz: " + e.getMessage());
            return "error";
        }
    }
    
    /**
     * USER STORY 2: Display list of all quizzes
     * USER STORY 6, TASK #19: Add "View questions" link to quiz list
     * 
     * Route: GET /quizzes
     * Template: quizzes-list.html
     * 
     * Shows all quizzes with their information and action buttons.
     * Each quiz has a "Manage Questions" button linking to questions list.
     */
    @GetMapping
    public String listQuizzes(Model model) {
        List<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "quizzes-list";
    }
    
    /**
     * Display a specific quiz
     * 
     * Route: GET /quizzes/{id}
     * Template: quiz-detail.html
     */
    @GetMapping("/{id}")
    public String viewQuiz(@PathVariable Long id, Model model) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        
        if (quiz.isEmpty()) {
            model.addAttribute("error", "Quiz not found");
            return "error";
        }
        
        model.addAttribute("quiz", quiz.get());
        return "quiz-detail";
    }
    
    /**
     * USER STORY 4: Delete a quiz
     * 
     * Route: POST /quizzes/{id}/delete
     * Redirect: /quizzes
     * 
     * Removes a quiz and all its associated questions from the system.
     */
    @PostMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
        }
        return "redirect:/quizzes";
    }
}
