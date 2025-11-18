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

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class QuestionRestController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    /**
     * Get all questions for a specific quiz
     */
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByQuiz(@PathVariable Long quizId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if (quiz.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Question> questions = questionService.getQuestionsByQuiz(quizId);
        return ResponseEntity.ok(questions);
    }

    /**
     * Get a specific question by ID
     */
    @GetMapping("/questions/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        return question.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new question for a quiz
     */
    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<Question> createQuestion(
            @PathVariable Long quizId,
            @RequestBody Question question) {
        try {
            Question createdQuestion = questionService.createQuestion(
                    question.getText(),
                    question.getDifficulty(),
                    quizId
            );
            return ResponseEntity.ok(createdQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing question
     */
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

    /**
     * Delete a question
     */
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isEmpty()) {
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
