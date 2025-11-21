package com.example.quizzer.question;

import java.util.List;
import java.util.Optional;

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

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.example.quizzer.quiz.QuizRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Questions", description = "Operations for retrieving and managing quiz questions")
public class QuestionRestController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

        // GET /api/quizzes/{quizId}/questions
        @Operation(summary = "Get questions for a quiz", description = "Returns all questions that belong to the quiz with the provided id.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Questions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz not found")
        })
        @GetMapping("/quizzes/{quizId}/questions")
        public ResponseEntity<List<Question>> getQuestionsByQuiz(@PathVariable Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            return ResponseEntity.notFound().build();
        }
        List<Question> questions = questionService.getQuestionsByQuiz(quizId);
        return ResponseEntity.ok(questions);
    }

        // GET /api/questions/{questionId}
        @Operation(summary = "Get a question", description = "Returns a single question by its id.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
        })
        @GetMapping("/questions/{questionId}")
        public ResponseEntity<?> getQuestion(@PathVariable Long questionId) {
        Optional<Question> question = questionService.getQuestionById(questionId);
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

        // POST /api/quizzes/{quizId}/questions
        @Operation(summary = "Create a question", description = "Creates a new question inside the quiz with the provided id.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question created successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz not found"),
            @ApiResponse(responseCode = "400", description = "Invalid question data")
        })
        @PostMapping("/quizzes/{quizId}/questions")
        public ResponseEntity<?> createQuestion(
            @PathVariable Long quizId,
            @RequestBody Question question) {
        try {
            Question createdQuestion = questionService.createQuestion(
                    question.getText(),
                    question.getDifficulty(),
                    quizId);
            return ResponseEntity.ok(createdQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

        // PUT /api/questions/{questionId}
        @Operation(summary = "Update a question", description = "Updates the question with the provided id.")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Question updated successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
        })
        @PutMapping("/questions/{questionId}")
        public ResponseEntity<Question> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody Question questionDetails) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Question existingQuestion = question.get();
        existingQuestion.setText(questionDetails.getText());
        if (questionDetails.getDifficulty() != null) {
            existingQuestion.setDifficulty(questionDetails.getDifficulty());
        }

        Question updatedQuestion = questionRepository.save(existingQuestion);
        return ResponseEntity.ok(updatedQuestion);
    }

        // DELETE /api/questions/{questionId}
        @Operation(summary = "Delete a question", description = "Deletes the question with the provided id.")
        @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
        })
        @DeleteMapping("/questions/{questionId}")
        public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }

        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

        // DELETE with quiz context: /api/quizzes/{quizId}/questions/{questionId}
        @Operation(summary = "Delete a question in a quiz", description = "Deletes the question with the provided id that belongs to the specified quiz.")
        @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Quiz or question not found")
        })
        @DeleteMapping("/quizzes/{quizId}/questions/{questionId}")
        public ResponseEntity<Void> deleteQuestionInQuiz(@PathVariable Long quizId, @PathVariable Long questionId) {
        if (!quizRepository.existsById(quizId)) {
            return ResponseEntity.notFound().build();
        }
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }

        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
