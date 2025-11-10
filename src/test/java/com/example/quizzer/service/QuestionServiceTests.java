package com.example.quizzer.service;

import com.example.quizzer.model.DifficultyLevel;
import com.example.quizzer.model.Question;
import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuestionRepository;
import com.example.quizzer.repository.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class QuestionServiceTests {
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private QuizService quizService;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    private Quiz testQuiz;
    
    @BeforeEach
    public void setUp() {
        // Clear data
        questionRepository.deleteAll();
        quizRepository.deleteAll();
        
        // Create test quiz
        testQuiz = quizService.createQuiz("Math Quiz", "Basic mathematics questions");
    }
    
    @Test
    public void testCreateQuestion() {
        // Act
        Question question = questionService.createQuestion(
                "What is 2 + 2?",
                "EASY",
                testQuiz.getId()
        );
        
        // Assert
        assertNotNull(question);
        assertNotNull(question.getId());
        assertEquals("What is 2 + 2?", question.getText());
        assertEquals(DifficultyLevel.EASY, question.getDifficulty());
        assertEquals(testQuiz.getId(), question.getQuiz().getId());
    }
    
    @Test
    public void testCreateQuestionWithNormalDifficulty() {
        // Act
        Question question = questionService.createQuestion(
                "What is the capital of France?",
                "NORMAL",
                testQuiz.getId()
        );
        
        // Assert
        assertNotNull(question);
        assertEquals(DifficultyLevel.NORMAL, question.getDifficulty());
    }
    
    @Test
    public void testCreateQuestionWithHardDifficulty() {
        // Act
        Question question = questionService.createQuestion(
                "Prove the Pythagorean theorem",
                "HARD",
                testQuiz.getId()
        );
        
        // Assert
        assertNotNull(question);
        assertEquals(DifficultyLevel.HARD, question.getDifficulty());
    }
    
    @Test
    public void testCreateQuestionWithInvalidQuiz() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.createQuestion(
                    "Some question",
                    "EASY",
                    999L
            );
        });
    }
    
    @Test
    public void testGetQuestionsByQuiz() {
        // Arrange
        questionService.createQuestion("Question 1", "EASY", testQuiz.getId());
        questionService.createQuestion("Question 2", "NORMAL", testQuiz.getId());
        questionService.createQuestion("Question 3", "HARD", testQuiz.getId());
        
        // Act
        List<Question> questions = questionService.getQuestionsByQuiz(testQuiz.getId());
        
        // Assert
        assertNotNull(questions);
        assertEquals(3, questions.size());
    }
    
    @Test
    public void testGetQuestionById() {
        // Arrange
        Question createdQuestion = questionService.createQuestion(
                "Test question",
                "EASY",
                testQuiz.getId()
        );
        
        // Act
        Optional<Question> foundQuestion = questionService.getQuestionById(createdQuestion.getId());
        
        // Assert
        assertTrue(foundQuestion.isPresent());
        assertEquals("Test question", foundQuestion.get().getText());
    }
    
    @Test
    public void testUpdateQuestion() {
        // Arrange
        Question createdQuestion = questionService.createQuestion(
                "Original question",
                "EASY",
                testQuiz.getId()
        );
        
        // Act
        Question updatedQuestion = questionService.updateQuestion(
                createdQuestion.getId(),
                "Updated question",
                "HARD"
        );
        
        // Assert
        assertNotNull(updatedQuestion);
        assertEquals("Updated question", updatedQuestion.getText());
        assertEquals(DifficultyLevel.HARD, updatedQuestion.getDifficulty());
    }
    
    @Test
    public void testUpdateNonExistentQuestion() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.updateQuestion(999L, "Text", "EASY");
        });
    }
    
    @Test
    public void testDeleteQuestion() {
        // Arrange
        Question createdQuestion = questionService.createQuestion(
                "To delete",
                "EASY",
                testQuiz.getId()
        );
        Long questionId = createdQuestion.getId();
        
        // Act
        questionService.deleteQuestion(questionId);
        
        // Assert
        Optional<Question> deletedQuestion = questionService.getQuestionById(questionId);
        assertTrue(deletedQuestion.isEmpty());
    }
    
    @Test
    public void testDeleteNonExistentQuestion() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            questionService.deleteQuestion(999L);
        });
    }
    
    @Test
    public void testGetAllQuestions() {
        // Create another quiz
        Quiz anotherQuiz = quizService.createQuiz("Science Quiz", "Science questions");
        
        // Arrange
        questionService.createQuestion("Q1", "EASY", testQuiz.getId());
        questionService.createQuestion("Q2", "NORMAL", testQuiz.getId());
        questionService.createQuestion("Q3", "EASY", anotherQuiz.getId());
        
        // Act
        List<Question> allQuestions = questionService.getAllQuestions();
        
        // Assert
        assertEquals(3, allQuestions.size());
    }
    
    @Test
    public void testMultipleQuestionsForSameQuiz() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            questionService.createQuestion(
                    "Question " + i,
                    i % 3 == 0 ? "HARD" : (i % 2 == 0 ? "NORMAL" : "EASY"),
                    testQuiz.getId()
            );
        }
        
        // Act
        List<Question> questions = questionService.getQuestionsByQuiz(testQuiz.getId());
        
        // Assert
        assertEquals(5, questions.size());
        
        // Verify all questions belong to the same quiz
        questions.forEach(q -> assertEquals(testQuiz.getId(), q.getQuiz().getId()));
    }
}
