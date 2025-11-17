package com.example.quizzer.quiz;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.quizzer.category.CategoryService;

@Controller
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final CategoryService categoryService;

    public QuizController(QuizService quizService, CategoryService categoryService) {
        this.quizService = quizService;
        this.categoryService = categoryService;
    }

    // USER STORY 1: Show create quiz form (Thymeleaf)
    @GetMapping("/new")
    public String showAddQuizForm(Model model) {
        model.addAttribute("categories", categoryService.listCategories());
        return "quiz-form";
    }

    // USER STORY 1: Handle quiz creation (Thymeleaf)
    @PostMapping
    public String addQuiz(@RequestParam String title,
            @RequestParam String description,
            @RequestParam String course,
        @RequestParam Long teacherId,
        @RequestParam(required = false) Long categoryId,
            Model model) { // Added Model parameter for error handling

        try {
            quizService.addQuiz(title, description, course, teacherId, categoryId);
            return "redirect:/quizzes";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating quiz: " + e.getMessage());
            // Return to the form with error message
            return "quiz-form";
        }
    }

    // USER STORY 2: List quizzes (Thymeleaf)
    @GetMapping
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.listQuizzes());
        return "quiz-list";
    }

    // USER STORY 2: View single quiz (Thymeleaf)
    @GetMapping("/{quizId}")
    public String viewQuiz(@PathVariable("quizId") Long quizId, Model model) {
        Optional<Quiz> quiz = quizService.getQuizById(quizId);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            return "quiz-detail";
        } else {
            return "error";
        }
    }

    // Show edit form
    @GetMapping("/{quizId}/edit")
    public String showEditQuizForm(@PathVariable("quizId") Long quizId, Model model) {
        System.out.println("=== DEBUG EDIT ===");
        System.out.println("Requested quiz ID: " + quizId);

        Optional<Quiz> quiz = quizService.getQuizById(quizId);
        System.out.println("Quiz found in database: " + quiz.isPresent());

        if (quiz.isPresent()) {
            Quiz q = quiz.get();
            System.out.println("Quiz details:");
            System.out.println("  - ID: " + q.getId());
            System.out.println("  - Title: " + q.getTitle());
            System.out.println("  - Description: " + q.getDescription());
            System.out.println("  - Course: " + q.getCourse());
            System.out.println("  - Teacher ID: " + q.getTeacherId());
            System.out.println("  - Published: " + q.isPublished());

            model.addAttribute("quiz", q);
            // Add categories for the category selector
            model.addAttribute("categories", categoryService.listCategories());
            System.out.println("Returning quiz-edit template");
            return "quiz-edit";
        } else {
            System.out.println("ERROR: No quiz found with ID: " + quizId);
            model.addAttribute("error", "Quiz not found with ID: " + quizId);
            return "error";
        }
    }

    @PostMapping("/{quizId}/edit")
    public String updateQuiz(@PathVariable("quizId") Long quizId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String course,
            @RequestParam(defaultValue = "false") boolean published,
            @RequestParam(required = false) Long categoryId,
            Model model) {
        try {
            quizService.updateQuiz(quizId, title, description, course, published, categoryId);
            // FIXED: Redirect to quiz list instead of individual quiz page
            return "redirect:/quizzes";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating quiz: " + e.getMessage());
            // Re-populate the quiz data for the form
            Optional<Quiz> quiz = quizService.getQuizById(quizId);
            if (quiz.isPresent()) {
                model.addAttribute("quiz", quiz.get());
            }
            return "quiz-edit";
        }
    }

    // USER STORY 4: Delete quiz (Thymeleaf)
    @PostMapping("/{quizId}/delete")
    public String deleteQuiz(@PathVariable("quizId") Long quizId) {
        quizService.deleteQuiz(quizId);
        return "redirect:/quizzes";
    }

    // REST endpoint for getting quiz by ID (if needed for APIs)
    // Note: API endpoints moved to QuizRestController to keep REST API under /api/*
}