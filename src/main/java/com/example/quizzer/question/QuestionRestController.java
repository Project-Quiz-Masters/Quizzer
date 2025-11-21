package com.example.quizzer.question;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow frontend requests
public class QuestionRestController {

  @Autowired
  private QuestionRepository questionRepository;

  @GetMapping("/quizzes/{quizId}/questions")
  public List<QuestionDTO> getQuestionsByQuizId(@PathVariable Long quizId) {
    return questionRepository.findByQuizId(quizId)
        .stream()
        .map(QuestionDTO::from)
        .collect(Collectors.toList());
  }
}
