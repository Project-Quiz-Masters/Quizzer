package com.example.quizzer.quiz;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow frontend requests from localhost:5173
public class QuizRestController {

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private QuizService quizService;

  // GET /api/quizzes - published quizzes
  @GetMapping("/quizzes")
  public List<Quiz> getPublishedQuizzes() {
    return quizRepository.findByPublishedTrue();
  }

  // POST /api/quizzes - create a new quiz via REST
  @PostMapping("/quizzes")
  public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
    if (quiz.getTeacherId() == null) {
      // Basic validation: teacherId required for now
      return ResponseEntity.badRequest().build();
    }
    Quiz saved = quizRepository.save(quiz);
    return ResponseEntity.ok(saved);
  }

  // GET /api/quizzes/{quizId} - get quiz by id
  @GetMapping("/quizzes/{quizId}")
  public ResponseEntity<Quiz> getQuizById(@PathVariable Long quizId) {
    Optional<Quiz> quiz = quizService.getQuizById(quizId);
    return quiz.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // PUT /api/quizzes/{quizId} - update quiz
  @PutMapping("/quizzes/{quizId}")
  public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quizDetails) {
    Quiz updated = quizService.updateQuiz(quizId, quizDetails);
    if (updated == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updated);
  }

  // DELETE /api/quizzes/{quizId} - delete quiz
  @DeleteMapping("/quizzes/{quizId}")
  public ResponseEntity<Void> deleteQuiz(@PathVariable Long quizId) {
    try {
      quizService.deleteQuiz(quizId);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // PUT /api/quizzes/{quizId}/category - assign or clear category for quiz
  public static class AssignCategoryDto {
    private Long categoryId;

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
  }

  @PutMapping("/quizzes/{quizId}/category")
  public ResponseEntity<Quiz> assignCategory(@PathVariable Long quizId, @RequestBody AssignCategoryDto payload) {
    if (payload == null) {
      return ResponseEntity.badRequest().build();
    }

    Long categoryId = payload.getCategoryId();
    Quiz updated = quizService.assignCategory(quizId, categoryId);
    if (updated == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updated);
  }
}
