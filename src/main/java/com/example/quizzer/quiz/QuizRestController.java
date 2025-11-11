package com.example.quizzer.quiz;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow frontend requests from localhost:5173
public class QuizRestController {

  @Autowired
  private QuizRepository quizRepository;

  @GetMapping("/quizzes")
  public List<Quiz> getPublishedQuizzes() {
    return quizRepository.findByPublishedTrue();
  }
}
