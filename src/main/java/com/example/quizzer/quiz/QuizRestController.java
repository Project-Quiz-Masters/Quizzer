package com.example.quizzer.quiz;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow frontend requests from localhost:5173
public class QuizRestController {

  @Autowired
  private QuizRepository quizRepository;

  @GetMapping("/quizzes")
  public List<QuizDTO> getPublishedQuizzes() {
    return quizRepository.findByPublishedTrue()
        .stream()
        .map(QuizDTO::from)
        .collect(Collectors.toList());
  }

  @GetMapping("/quizzes/{id}")
  public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
    return quizRepository.findById(id)
        .map(quiz -> ResponseEntity.ok(QuizDTO.from(quiz)))
        .orElse(ResponseEntity.notFound().build());
  }
}
