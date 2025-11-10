package com.example.quizzer.question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizService;

@RestController
@RequestMapping("/api/quizzes")
public class QuizRestController {
    
    @Autowired
    private QuizService quizService;
    
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }
    
    @GetMapping("/teacher/{teacherId}")
    public List<Quiz> getQuizzesByTeacher(@PathVariable Long teacherId) {
        return quizService.getQuizzesByTeacher(teacherId);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        return quizService.getQuizById(id)
                .map(quiz -> ResponseEntity.ok(quiz))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @RequestBody Quiz quizDetails) {
        Quiz updatedQuiz = quizService.updateQuiz(id, quizDetails);
        return updatedQuiz != null ? ResponseEntity.ok(updatedQuiz) : ResponseEntity.notFound().build();
    }
    
    // DELETE ENDPOINT - FIXED VERSION
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteQuiz(
            @PathVariable Long id,
            @RequestHeader("X-Teacher-Id") Long teacherId) { // Add this parameter
        
        boolean deleted = quizService.deleteQuiz(id, teacherId);
        
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Quiz deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Quiz not found or unauthorized");
            return ResponseEntity.status(404).body(response);
        }
    }
    
    // Alternative delete endpoint without teacher validation
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteQuizAdmin(@PathVariable Long id) {
        boolean deleted = quizService.deleteQuiz(id);
        
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Quiz deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Quiz not found");
            return ResponseEntity.status(404).body(response);
        }
    }
}