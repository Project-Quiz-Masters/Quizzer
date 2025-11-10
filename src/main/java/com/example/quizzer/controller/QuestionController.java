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

/**
 * QuestionController
 * 
 * USER STORY 5: Add question to a quiz
 * USER STORY 6: View list of quiz's questions
 * TASK #14: Connect form to database
 * TASK #18: Add endpoint to list questions of a quiz
 * 
 * Handles HTTP requests for question-related operations.
 * Routes:
 * - GET  /quizzes/{quizId}/questions/add      -> Show add question form
 * - POST /quizzes/{quizId}/questions          -> Create new question
 * - GET  /quizzes/{quizId}/questions          -> Show list of questions
 * - POST /quizzes/{quizId}/questions/{id}/delete -> Delete question
 */
@Controller
@RequestMapping("/quizzes")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * USER STORY 5, TASK #12: Display the form to add a question to a quiz
     * 
     * Route: GET /quizzes/{quizId}/questions/add
     * Template: add-question.html
     * 
     * Shows the form where teachers can enter question text and select difficulty.
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
     * USER STORY 5, TASK #14: Handle form submission to add a question
     * 
     * Route: POST /quizzes/{quizId}/questions
     * Accepts: text, difficulty
     * Redirect: /quizzes/{quizId}/questions (list view)
     * 
     * Creates a new question and saves it to the database.
     * Then redirects to the questions list to confirm creation.
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
     * USER STORY 6, TASK #18: Display all questions for a quiz
     * USER STORY 6, TASK #20: Create page to list questions
     * 
     * Route: GET /quizzes/{quizId}/questions
     * Template: questions-list.html
     * 
     * Shows all questions belonging to a specific quiz.
     * Displays question text, difficulty level, and delete option.
     * Shows "No Questions Yet" message if quiz is empty.
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
     * USER STORY 7 (Future): Delete a quiz's question
     * 
     * Route: POST /quizzes/{quizId}/questions/{questionId}/delete
     * Redirect: /quizzes/{quizId}/questions (list view)
     * 
     * Removes a question from a quiz.
     * Then redirects back to the questions list.
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
