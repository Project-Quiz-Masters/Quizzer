package com.example.quizzer;

import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnswerOptionRestController {
    
    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionRestController(AnswerOptionRepository answerOptionRepository, QuestionRepository questionRepository) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<List<AnswerOption>> getAnswerOptions(@PathVariable @NonNull Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerOptionRepository.findByQuestionId(questionId));

    }
    
}
