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

    // USER STORY 3: Show edit quiz form (Thymeleaf)
    @GetMapping("/{id}/edit")
    public String showEditQuizForm(@PathVariable Long id, Model model) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            model.addAttribute("quiz", quiz.get());
            return "quiz-edit";
        } else {
            return "error";
        }
    }

    // USER STORY 3: Handle quiz update (Thymeleaf)
    @PostMapping("/{id}/edit")
    public String updateQuiz(@PathVariable Long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String course,
            @RequestParam(defaultValue = "false") boolean published) {

        // You'll need to add this method to QuizService
        quizService.updateQuiz(id, title, description, course, published);
        return "redirect:/quizzes/" + id;
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