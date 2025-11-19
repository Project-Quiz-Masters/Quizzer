package com.example.quizzer.answeroption;

import com.example.quizzer.question.QuestionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
@Tag(name = "Answer Options", description = "Operations for managing answer options")
public class AnswerOptionRestController {

    private final AnswerOptionService answerOptionService;

    private final AnswerOptionRepository answerOptionRepository;
    private final QuestionRepository questionRepository;

    public AnswerOptionRestController(AnswerOptionRepository answerOptionRepository,
            QuestionRepository questionRepository, AnswerOptionService answerOptionService) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
        this.answerOptionService = answerOptionService;
    }

    @Operation(summary = "List answer options", description = "Returns answer options for a question.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Answer options retrieved"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @GetMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<List<AnswerOption>> getAnswerOptions(@PathVariable @NonNull Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerOptionRepository.findByQuestionId(questionId));

    }

    @Operation(summary = "Add answer option", description = "Adds an answer option to the specified question.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Answer option added"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping("/questions/{questionId}/answer-options")
    public ResponseEntity<AnswerOption> addAnswerOption(
            @PathVariable Long questionId, @RequestBody AnswerOption answerOption) {
        if (!questionRepository.existsById(questionId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(answerOptionService.addAnswerOption(answerOption));
    }

    // @DeleteMapping("/answer-options/{answerOptionId}")
    // public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long
    // answerOptionId){
    // answerOptionService.deleteAnswerOption(answerOptionId);
    // return ResponseEntity.noContent().build();
    // }

    // Osman's version
    @Operation(summary = "Delete an answer option", description = "Deletes the answer option with the given id for the specified question.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Answer option deleted"),
            @ApiResponse(responseCode = "404", description = "Question or answer option not found")
    })
    @DeleteMapping("/questions/{questionId}/answer-options/{answerOptionId}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable @NonNull Long questionId,
            @PathVariable @NonNull Long answerOptionId) {
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
