package com.example.quizzer.studentanswer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.quizzer.dto.StudentAnswerDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/student-answers")
@CrossOrigin(origins = "*")
@Tag(name = "Student Answers", description = "Endpoints for submitting and managing student answers")

public class StudentAnswerRestController {
    private final StudentAnswerService studentAnswerService;

    public StudentAnswerRestController(StudentAnswerService studentAnswerService) {
        this.studentAnswerService = studentAnswerService;
    }

    @Operation(summary = "Submit an answer", description = "Allows a student to submit an answer option for a question")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Answer submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (missing answerOptionId or quiz not published)"),
            @ApiResponse(responseCode = "404", description = "Answer option not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping
    public ResponseEntity<StudentAnswer> submitAnswer(@RequestBody StudentAnswerDTO dto) {
        try {
            StudentAnswer saved = studentAnswerService.createStudentAnswer(dto.getAnswerOptionId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (ResponseStatusException ex) {
            // Return the same status code and message from the service
            return ResponseEntity.status(ex.getStatusCode()).body(null);
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
