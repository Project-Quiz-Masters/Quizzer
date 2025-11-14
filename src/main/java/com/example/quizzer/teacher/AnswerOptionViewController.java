package com.example.quizzer.teacher;

import com.example.quizzer.answeroption.AnswerOption;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class AnswerOptionViewController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/api";

    @GetMapping("/questions/{id}/answers")
    public String showAnswerOptions(@PathVariable Long id, Model model) {
        Map question = restTemplate.getForObject(BASE_URL + "/questions/" + id, Map.class);
        List<Map<String, Object>> answers =
                restTemplate.getForObject(BASE_URL + "/questions/" + id + "/answer-options", List.class);

        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        return "answerOptions";
    }

    @GetMapping("/questions/{id}/answers/add")
    public String showAddAnswerOptionForm(@PathVariable Long id, Model model) {
        model.addAttribute("questionId", id);
        model.addAttribute("answerOption", new AnswerOption());
        return "addAnswerOption";
    }

    @PostMapping("/questions/{id}/answers/add")
    public String addAnswerOption(@PathVariable Long id, @ModelAttribute AnswerOption answerOption) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AnswerOption> request = new HttpEntity<>(answerOption, headers);
        restTemplate.postForObject(BASE_URL + "/questions/" + id + "/answer-options", request, AnswerOption.class);

        return "redirect:/teacher/questions/" + id + "/answers";
    }
}
