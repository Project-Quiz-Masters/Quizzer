package com.example.quizzer.answeroption;

import com.example.quizzer.question.Question;
import com.example.quizzer.question.QuestionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AnswerOptionRestController {

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionService answerOptionService;

    public AnswerOptionRestController(
            AnswerOptionRepository answerOptionRepository,
            QuestionRepository questionRepository,
            AnswerOptionService answerOptionService
    ) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
        this.answerOptionService = answerOptionService;
    }

    // ✅ GET all answers for a question
    @GetMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<List<AnswerOption>> getAnswerOptions(@PathVariable @NonNull Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerOptionRepository.findByQuestionId(questionId));
    }

    // ✅ FIXED: Add an answer option (now sets question)
    @PostMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<AnswerOption> addAnswerOption(
            @PathVariable Long questionId,
            @RequestBody AnswerOption answerOption
    ) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        // ✅ link the answer option with the question
        answerOption.setQuestion(question);

        AnswerOption saved = answerOptionRepository.save(answerOption);
        return ResponseEntity.ok(saved);
    }

    // ✅ DELETE
    @DeleteMapping("/answer-options/{id}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long id) {
        if (!answerOptionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        answerOptionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
