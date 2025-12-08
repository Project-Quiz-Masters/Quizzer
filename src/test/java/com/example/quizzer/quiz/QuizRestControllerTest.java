package com.example.quizzer.quiz;

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class QuizRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizRepository quizRepository;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        quizRepository.deleteAll();
    }

    // ✅ 1. getAllQuizzesReturnsEmptyListWhenNoQuizzesExist
    @Test
    void getAllQuizzesReturnsEmptyListWhenNoQuizzesExist() throws Exception {
        // Act + Assert
        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ✅ 2. getAllQuizzesReturnsListOfQuizzesWhenPublishedQuizzesExist
    @Test
    void getAllQuizzesReturnsListOfQuizzesWhenPublishedQuizzesExist() throws Exception {
        // Arrange
        Quiz quiz1 = new Quiz("Quiz 1", "Desc 1", "Course", 1L);
        quiz1.setPublished(true);
        Quiz quiz2 = new Quiz("Quiz 2", "Desc 2", "Course", 1L);
        quiz2.setPublished(true);
        quizRepository.saveAll(List.of(quiz1, quiz2));

        // Act + Assert
        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].title").value(containsInAnyOrder("Quiz 1", "Quiz 2")));
    }

    // ✅ 3. getAllQuizzesDoesNotReturnUnpublishedQuizzes
    @Test
    void getAllQuizzesDoesNotReturnUnpublishedQuizzes() throws Exception {
        // Arrange
        Quiz publishedQuiz = new Quiz("Published Quiz", "Desc", "Course", 1L);
        publishedQuiz.setPublished(true);
        Quiz unpublishedQuiz = new Quiz("Unpublished Quiz", "Desc", "Course", 1L);
        quizRepository.saveAll(List.of(publishedQuiz, unpublishedQuiz));

        // Act + Assert
        mockMvc.perform(get("/api/quizzes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Published Quiz"));
    }
}
