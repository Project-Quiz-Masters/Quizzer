package com.example.quizzer.quiz;

import org.springframework.web.bind.annotation.*;

import com.example.quizzer.answeroption.AnswerOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow frontend requests from localhost:5173
public class QuizRestController {

  @Autowired
  private QuizRepository quizRepository;

  @GetMapping("/quizzes/published")
  public List<Quiz> getPublishedQuizzes() {
    return quizRepository.findByPublishedTrue();
  }

  @GetMapping("/public/quizzes/{id}")
  public ResponseEntity<?> getQuiz(@PathVariable Long id) {

    Quiz quiz = quizRepository.findById(id).orElse(null);
    if (quiz == null) {
      return ResponseEntity.status(404).body("Quiz not found");

    }
    var quizDto = java.util.Map.of(
      "id", quiz.getId(),
        "title", quiz.getTitle(),
        "description", quiz.getDescription(),
        "questions", quiz.getQuestions().stream().map(q ->
            java.util.Map.of(
                "id", q.getId(),
                "text", q.getText(),
                "options", q.getAnswerOptions().stream()
                    .map(AnswerOption::getText)
                    .toList()
            )
        ).toList()
    );
    return ResponseEntity.ok(quizDto);
  }

  @PostMapping("/quizzes/{id}/submit")
@CrossOrigin(origins = "*")
public ResponseEntity<?> submitQuiz(
  @PathVariable Long id,
  @RequestBody Map<Long, Object> answers
) {
  Quiz quiz = quizRepository.findById(id).orElse(null);

  if (quiz == null) {
    return ResponseEntity.status(404).body("Quiz not found");
  }

  int total = quiz.getQuestions().size();
  int score = 0;

  for (var question : quiz.getQuestions()) {
    Object raw = answers.get(question.getId());
    if (raw == null) continue;

    List<String> userSelected =
        raw instanceof List
            ? (List<String>) raw
            : List.of(raw.toString());

    List<String> correctOptions = question.getAnswerOptions().stream()
      .filter(AnswerOption::isCorrect)
      .map(AnswerOption::getText)
      .toList();

    if (new HashSet<>(userSelected).equals(new HashSet<>(correctOptions))) {
      score++;
    }
  }

  return ResponseEntity.ok(Map.of(
    "quizId", id,
    "score", score,
    "total", total
  ));

  }
}
