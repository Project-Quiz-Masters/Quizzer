package com.example.quizzer.question;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class QuestionRestController {

    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // GET /api/quizzes/{quizId}/questions
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<Question>> listQuestions(@PathVariable Long quizId) {
        List<Question> questions = questionService.getQuestionsByQuiz(quizId);
        return ResponseEntity.ok(questions);
    }

    // POST /api/quizzes/{quizId}/questions
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<Question> createQuestion(@PathVariable Long quizId, @RequestBody Question payload) {
        // The service method createQuestion will validate quiz existence
        try {
            Question created = questionService.createQuestion(payload.getText(), payload.getDifficulty(), quizId);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // // GET /api/questions/{questionId}
    // @GetMapping("/questions/{questionId}")
    // public ResponseEntity<Question> getQuestion(@PathVariable Long questionId) {
    //     Optional<Question> q = questionService.getQuestionById(questionId);
    //     return q.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // DELETE /api/quizzes/{quizId}/questions/{questionId}
    @DeleteMapping("/quizzes/{quizId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long quizId, @PathVariable Long questionId) {
        // We don't strictly need quizId to delete, but keep it in path for clarity
        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
