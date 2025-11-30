package com.example.quizzer.quiz;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow frontend requests from localhost:5173
@Tag(name = "Quizzes", description = "Operations for retrieving and managing quizzes")
public class QuizRestController {

  @Autowired
  private QuizRepository quizRepository;

  @Autowired
  private QuizService quizService;

  // GET /api/quizzes - published quizzes
  @Operation(summary = "List published quizzes", description = "Returns all published quizzes")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully")
  })
  @GetMapping("/quizzes")
  public List<QuizDTO> getPublishedQuizzes() {
    return quizRepository.findByPublishedTrue()
        .stream()
        .map(QuizDTO::from)
        .collect(Collectors.toList());
  }

  // POST /api/quizzes - create a new quiz via REST
  @Operation(summary = "Create a quiz", description = "Creates a new quiz")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Quiz created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid quiz data")
  })
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
  @Operation(summary = "Get a quiz", description = "Returns the quiz with the specified id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Quiz retrieved successfully"),
      @ApiResponse(responseCode = "404", description = "Quiz not found")
  })
  @GetMapping("/quizzes/{quizId}")
  public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long quizId) {
    Optional<Quiz> quiz = quizService.getQuizById(quizId);
    return quiz.map(q -> ResponseEntity.ok(QuizDTO.from(q))).orElseGet(() -> ResponseEntity.notFound().build());
  }

  // PUT /api/quizzes/{quizId} - update quiz
  @Operation(summary = "Update a quiz", description = "Updates the quiz with the specified id")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Quiz updated successfully"),
      @ApiResponse(responseCode = "404", description = "Quiz not found")
  })
  @PutMapping("/quizzes/{quizId}")
  public ResponseEntity<Quiz> updateQuiz(@PathVariable Long quizId, @RequestBody Quiz quizDetails) {
    Quiz updated = quizService.updateQuiz(quizId, quizDetails);
    if (updated == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updated);
  }

  // DELETE /api/quizzes/{quizId} - delete quiz
  @Operation(summary = "Delete a quiz", description = "Deletes the quiz with the specified id")
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Quiz deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Quiz not found")
  })
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

    public Long getCategoryId() {
      return categoryId;
    }

    public void setCategoryId(Long categoryId) {
      this.categoryId = categoryId;
    }
  }

  @Operation(summary = "Assign category", description = "Assigns or clears a category for the specified quiz")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Category assigned to quiz"),
      @ApiResponse(responseCode = "404", description = "Quiz or category not found"),
      @ApiResponse(responseCode = "400", description = "Invalid payload")
  })
  @PutMapping("/quizzes/{quizId}/category")
  public ResponseEntity<Quiz> assignCategory(@PathVariable Long quizId, @RequestBody AssignCategoryDto payload) {
    if (payload == null) {
      return ResponseEntity.badRequest().build();
    }

    Long categoryId = payload.getCategoryId();
    try {
      Quiz updated = quizService.assignCategoryToQuiz(quizId, categoryId);
      return ResponseEntity.ok(updated);
    } catch (IllegalArgumentException e) {
      // Differentiate messages if needed; return 404 for not-found cases
      return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).build();
    }
  }
}
