package com.example.quizzer.controller;

import com.example.quizzer.model.DifficultyLevel;
import com.example.quizzer.model.Question;
import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuizRepository;
import com.example.quizzer.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quizzes")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * Display the form to add a question to a quiz
     */
    @GetMapping("/{quizId}/questions/add")
    public String showAddQuestionForm(@PathVariable Long quizId, Model model) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        
        if (quiz.isEmpty()) {
            return "error";
        }
        
        model.addAttribute("quiz", quiz.get());
        model.addAttribute("difficulties", DifficultyLevel.values());
        
        return "add-question";
    }
    
    /**
     * Handle the form submission to add a question
     */
    @PostMapping("/{quizId}/questions")
    public String addQuestion(@PathVariable Long quizId,
                             @RequestParam String text,
                             @RequestParam(defaultValue = "NORMAL") String difficulty,
                             Model model) {
        try {
            questionService.createQuestion(text, difficulty, quizId);
            return "redirect:/quizzes/" + quizId + "/questions";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    
    /**
     * Display all questions for a quiz
     */
    @GetMapping("/{quizId}/questions")
    public String listQuestions(@PathVariable Long quizId, Model model) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        
        if (quiz.isEmpty()) {
            return "error";
        }
        
        List<Question> questions = questionService.getQuestionsByQuiz(quizId);
        
        model.addAttribute("quiz", quiz.get());
        model.addAttribute("questions", questions);
        
        return "questions-list";
    }
    
    /**
     * Delete a question
     */
    @PostMapping("/{quizId}/questions/{questionId}/delete")
    public String deleteQuestion(@PathVariable Long quizId,
                                @PathVariable Long questionId) {
        try {
            questionService.deleteQuestion(questionId);
        } catch (IllegalArgumentException e) {
            // Log error but continue
        }
        return "redirect:/quizzes/" + quizId + "/questions";
    }
}
