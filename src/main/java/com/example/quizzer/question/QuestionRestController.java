package com.example.quizzer.question;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Questions", description = "Operations for retrieving and managing quiz questions")
public class QuestionRestController {

    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // ------------------------------------------------------------
    // GET /api/quizzes/{quizId}/questions
    // ------------------------------------------------------------
    @Operation(summary = "Get questions for a quiz", description = "Returns all questions that belong to the quiz with the provided id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<Question>> listQuestions(@PathVariable Long quizId) {
        List<Question> questions = questionService.getQuestionsByQuiz(quizId);
        return ResponseEntity.ok(questions);
    }

    // ------------------------------------------------------------
    // POST /api/quizzes/{quizId}/questions
    // ------------------------------------------------------------
    @Operation(summary = "Create a question", description = "Creates a new question inside the quiz with the provided id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question created successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz not found"),
            @ApiResponse(responseCode = "400", description = "Invalid question data")
    })
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<Question> createQuestion(
            @PathVariable Long quizId,
            @RequestBody Question payload) {

        try {
            Question created = questionService.createQuestion(
                    payload.getText(),
                    payload.getDifficulty(),
                    quizId);

            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ------------------------------------------------------------
    // DELETE /api/quizzes/{quizId}/questions/{questionId}
    // ------------------------------------------------------------
    @Operation(summary = "Delete a question", description = "Deletes the question with the provided id.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/quizzes/{quizId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long quizId,
            @PathVariable Long questionId) {

        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
