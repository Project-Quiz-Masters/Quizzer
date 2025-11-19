package com.example.quizzer.question;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;

/**
 * QuestionController
 * 
 * Handles HTTP requests for question-related operations.
 * Routes:
 * - GET /quizzes/{quizId}/questions/add -> Show add question form
 * - POST /quizzes/{quizId}/questions -> Create new question
 * - GET /quizzes/{quizId}/questions -> Show list of questions
 * - POST /quizzes/{quizId}/questions/{questionId}/delete -> Delete question
 */
@Controller
@RequestMapping("/quizzes/{quizId}/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuizRepository quizRepository;

    /**
     * Display the form to add a question to a quiz
     */
    @GetMapping("/add")
    public String showAddQuestionForm(@PathVariable Long quizId, Model model) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);

        if (quiz.isEmpty()) {
            model.addAttribute("error", "Quiz not found");
            return "error";
        }

        model.addAttribute("quiz", quiz.get());
        model.addAttribute("difficultyOptions", List.of("Easy", "Normal", "Hard")); // Optional for UI dropdown

        return "add-question";
    }

    /**
     * Handle form submission to add a question
     */
    @PostMapping
    public String addQuestion(@PathVariable Long quizId,
            @RequestParam String text,
            @RequestParam(defaultValue = "Normal") String difficulty,
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
    @GetMapping
    public String listQuestions(@PathVariable Long quizId, Model model) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);

        if (quiz.isEmpty()) {
            model.addAttribute("error", "Quiz not found");
            return "error";
        }

        List<Question> questions = questionService.getQuestionsByQuiz(quizId);

        model.addAttribute("quiz", quiz.get());
        model.addAttribute("questions", questions);

        return "questions-list";
    }

    /**
     * Delete a quiz's question
     */
    @PostMapping("/{questionId}/delete")
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
