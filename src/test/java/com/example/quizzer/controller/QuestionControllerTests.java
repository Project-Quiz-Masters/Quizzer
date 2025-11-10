package com.example.quizzer.controller;

import com.example.quizzer.model.DifficultyLevel;
import com.example.quizzer.model.Question;
import com.example.quizzer.model.Quiz;
import com.example.quizzer.service.QuestionService;
import com.example.quizzer.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class QuestionControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private QuizService quizService;
    
    @Autowired
    private QuestionService questionService;
    
    private Quiz testQuiz;
    
    @BeforeEach
    public void setUp() {
        testQuiz = quizService.createQuiz("Test Quiz", "A test quiz for controller testing");
    }
    
    @Test
    public void testShowAddQuestionForm() throws Exception {
        mockMvc.perform(get("/quizzes/{quizId}/questions/add", testQuiz.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-question"))
                .andExpect(model().attributeExists("quiz"))
                .andExpect(model().attributeExists("difficulties"));
    }
    
    @Test
    public void testShowAddQuestionFormWithInvalidQuiz() throws Exception {
        mockMvc.perform(get("/quizzes/{quizId}/questions/add", 999L))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    
    @Test
    public void testAddQuestion() throws Exception {
        mockMvc.perform(post("/quizzes/{quizId}/questions", testQuiz.getId())
                .param("text", "What is Spring Boot?")
                .param("difficulty", "NORMAL"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quizzes/" + testQuiz.getId() + "/questions"));
    }
    
    @Test
    public void testAddQuestionWithEasyDifficulty() throws Exception {
        mockMvc.perform(post("/quizzes/{quizId}/questions", testQuiz.getId())
                .param("text", "Simple question")
                .param("difficulty", "EASY"))
                .andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void testAddQuestionWithHardDifficulty() throws Exception {
        mockMvc.perform(post("/quizzes/{quizId}/questions", testQuiz.getId())
                .param("text", "Complex question")
                .param("difficulty", "HARD"))
                .andExpect(status().is3xxRedirection());
    }
    
    @Test
    public void testAddQuestionWithInvalidQuiz() throws Exception {
        mockMvc.perform(post("/quizzes/{quizId}/questions", 999L)
                .param("text", "Question text")
                .param("difficulty", "NORMAL"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    
    @Test
    public void testListQuestions() throws Exception {
        // Arrange
        questionService.createQuestion("Question 1", "EASY", testQuiz.getId());
        questionService.createQuestion("Question 2", "NORMAL", testQuiz.getId());
        
        // Act & Assert
        mockMvc.perform(get("/quizzes/{quizId}/questions", testQuiz.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("questions-list"))
                .andExpect(model().attributeExists("quiz"))
                .andExpect(model().attributeExists("questions"));
    }
    
    @Test
    public void testListQuestionsWithInvalidQuiz() throws Exception {
        mockMvc.perform(get("/quizzes/{quizId}/questions", 999L))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    
    @Test
    public void testListQuestionsDisplaysCorrectCount() throws Exception {
        // Arrange
        for (int i = 1; i <= 3; i++) {
            questionService.createQuestion("Question " + i, "EASY", testQuiz.getId());
        }
        
        // Act & Assert
        mockMvc.perform(get("/quizzes/{quizId}/questions", testQuiz.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("questions", hasSize(3)));
    }
    
    @Test
    public void testDeleteQuestion() throws Exception {
        // Arrange
        Question question = questionService.createQuestion("To delete", "EASY", testQuiz.getId());
        
        // Act & Assert
        mockMvc.perform(post("/quizzes/{quizId}/questions/{questionId}/delete", testQuiz.getId(), question.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/quizzes/" + testQuiz.getId() + "/questions"));
    }
    
    @Test
    public void testDeleteNonExistentQuestion() throws Exception {
        // This should not throw an error - just redirect
        mockMvc.perform(post("/quizzes/{quizId}/questions/{questionId}/delete", testQuiz.getId(), 999L))
                .andExpect(status().is3xxRedirection());
    }
}
