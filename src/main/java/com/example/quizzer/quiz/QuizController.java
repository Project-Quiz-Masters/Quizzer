package com.example.quizzer.quiz;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // USER STORY 1: Show create quiz form (Thymeleaf)
    @GetMapping("/new")
    public String showAddQuizForm() {
        return "quiz-form";
    }

    // USER STORY 1: Handle quiz creation (Thymeleaf)
@PostMapping
public String addQuiz(@RequestParam String title,
                     @RequestParam String description,
                     @RequestParam String course,
                     @RequestParam Long teacherId,
                     Model model) { // Added Model parameter for error handling

    try {
        quizService.addQuiz(title, description, course, teacherId);
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
    @GetMapping("/{id}")
    public String viewQuiz(@PathVariable Long id, Model model) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            return "quiz-detail";
        } else {
            return "error";
        }
    }
// Show edit form
@GetMapping("/{id}/edit")
public String showEditQuizForm(@PathVariable Long id, Model model) {
    System.out.println("=== DEBUG EDIT ===");
    System.out.println("Requested quiz ID: " + id);
    
    Optional<Quiz> quiz = quizService.getQuizById(id);
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
        System.out.println("Returning quiz-edit template");
        return "quiz-edit";
    } else {
        System.out.println("ERROR: No quiz found with ID: " + id);
        model.addAttribute("error", "Quiz not found with ID: " + id);
        return "error";
    }
}

@PostMapping("/{id}/edit")
public String updateQuiz(@PathVariable Long id,
                        @RequestParam String title,
                        @RequestParam String description,
                        @RequestParam String course,
                        @RequestParam(defaultValue = "false") boolean published,
                        Model model) {
    try {
        quizService.updateQuiz(id, title, description, course, published);
        // FIXED: Redirect to quiz list instead of individual quiz page
        return "redirect:/quizzes";
    } catch (Exception e) {
        model.addAttribute("error", "Error updating quiz: " + e.getMessage());
        // Re-populate the quiz data for the form
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
        }
        return "quiz-edit";
    }
}

    // USER STORY 4: Delete quiz (Thymeleaf)
    @PostMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/quizzes";
    }

    // REST endpoint for getting quiz by ID (if needed for APIs)
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Quiz> getQuizByIdApi(@PathVariable Long id) {
        return quizService.getQuizById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // REST endpoint for updating quiz (if needed for APIs)
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Quiz> updateQuizApi(@PathVariable Long id, @RequestBody Quiz quizDetails) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quizDetails);
        return updatedQuiz != null ? ResponseEntity.ok(updatedQuiz) : ResponseEntity.notFound().build();
    }

    // REST endpoint for deleting quiz (if needed for APIs)
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteQuizApi(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return ResponseEntity.noContent().build();
    }
}