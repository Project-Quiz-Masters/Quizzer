package com.example.quizzer.controller;

import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quizzes")
public class QuizController {
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * Display the form to create a new quiz
     */
    @GetMapping("/new")
    public String showCreateQuizForm() {
        return "create-quiz";
    }
    
    /**
     * Handle the form submission to create a new quiz
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
     * Display all quizzes
     */
    @GetMapping
    public String listQuizzes(Model model) {
        List<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "quizzes-list";
    }
    
    /**
     * Display a specific quiz
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
     * Delete a quiz
     */
    @PostMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
        }
        return "redirect:/quizzes";
    }
}
