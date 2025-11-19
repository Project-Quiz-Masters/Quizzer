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
    
    // @DeleteMapping("/answer-options/{answerOptionId}")
    // public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long answerOptionId){
    //     answerOptionService.deleteAnswerOption(answerOptionId);
    //     return ResponseEntity.noContent().build();
    // }

    //Osman's version
    @DeleteMapping("/questions/{questionId}/answer-options/{answerOptionId}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable @NonNull Long questionId, @PathVariable @NonNull Long answerOptionId) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }

        if (!answerOptionRepository.existsById(answerOptionId)) {
            return ResponseEntity.notFound().build();
        }

        answerOptionRepository.deleteById(answerOptionId);
        return ResponseEntity.noContent().build();
    }
}
