package com.example.quizzer.answeroption;

import com.example.quizzer.question.QuestionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnswerOptionRestController {

    private final AnswerOptionService answerOptionService;
    
    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionRestController(AnswerOptionRepository answerOptionRepository, QuestionRepository questionRepository, AnswerOptionService answerOptionService) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
        this.answerOptionService = answerOptionService;
    }

    @GetMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<List<AnswerOption>> getAnswerOptions(@PathVariable @NonNull Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerOptionRepository.findByQuestionId(questionId));

    }

    @PostMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<AnswerOption> addAnswerOption(
        @PathVariable Long questionId, @RequestBody AnswerOption answerOption) {
            if (!questionRepository.existsById(questionId)) {
                return ResponseEntity.notFound().build();
            } return ResponseEntity.ok(answerOptionService.addAnswerOption(answerOption));
    }
    
    @DeleteMapping("/answer-options/{id}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long id){
        answerOptionService.deleteAnswerOption(id);
        return ResponseEntity.noContent().build();
    }
}
