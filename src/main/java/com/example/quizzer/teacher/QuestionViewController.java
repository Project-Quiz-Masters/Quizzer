package com.example.quizzer.teacher;

import com.example.quizzer.question.Question;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class QuestionViewController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api";

    @GetMapping("/questions")
    public String showQuestions(Model model) {
        List<Map<String, Object>> questions =
                restTemplate.getForObject(BASE_URL + "/questions/quiz/1", List.class);
        model.addAttribute("questions", questions);
        return "questions";
    }

    @GetMapping("/questions/add")
    public String showAddQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        return "addQuestion";
    }

    @PostMapping("/questions/add")
    public String addQuestion(@ModelAttribute Question question) {
        restTemplate.postForObject(BASE_URL + "/questions", question, Question.class);
        return "redirect:/teacher/questions";
    }
}
