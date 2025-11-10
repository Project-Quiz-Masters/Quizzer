package com.example.quizzer.quiz;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // USER STORY 1: add quiz (form submit)
    @PostMapping("/quizzes")
    public String addQuiz(@RequestParam String title,
            @RequestParam String description,
            @RequestParam String course,
            @RequestParam String teacherId) {

        quizService.addQuiz(title, description, course, teacherId);

        return "redirect:/quizzes";
    }
    // USER STORY 2: list quizzes
    @GetMapping("/quizzes")
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.listQuizzes());
        return "quiz-list"; // thymeleaf template name (quiz-list.html)
    }

    // create page to display the form
    @GetMapping("/quizzes/new")
    public String showAddQuizForm() {
        return "quiz-form"; // thymeleaf template name (quiz-form.html)
    }
}
