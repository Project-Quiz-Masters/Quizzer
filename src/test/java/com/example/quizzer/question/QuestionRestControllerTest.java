package com.example.quizzer.question;

import com.example.quizzer.answeroption.AnswerOption;
import com.example.quizzer.answeroption.AnswerOptionRepository;
import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QuestionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    private Quiz quiz;

    @BeforeEach
    void setup() {
        quizRepository.deleteAll();
        questionRepository.deleteAll();
        // Note: AnswerOptionRepository may depend on questions; clear after questions
        answerOptionRepository.deleteAll();
        quiz = new Quiz("Test Quiz", "Desc", "CS101", 1L);
        quiz = quizRepository.save(quiz);
    }

    @Test
    void getQuestionsByQuizIdReturnsEmptyListWhenQuizDoesNotHaveQuestions() throws Exception {
        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/questions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getQuestionsByQuizIdReturnsListOfQuestionsWhenQuizHasQuestions() throws Exception {
        Question q1 = new Question();
        q1.setText("What is 2+2?");
        q1.setDifficulty("Easy");
        q1.setQuiz(quiz);
        AnswerOption a11 = new AnswerOption();
        a11.setText("3");
        a11.setCorrect(false);
        a11.setQuestion(q1);
        q1.getAnswerOptions().add(a11);

        AnswerOption a12 = new AnswerOption();
        a12.setText("4");
        a12.setCorrect(true);
        a12.setQuestion(q1);
        q1.getAnswerOptions().add(a12);

        q1 = questionRepository.save(q1);

        Question q2 = new Question();
        q2.setText("Capital of France?");
        q2.setDifficulty("Normal");
        q2.setQuiz(quiz);
        AnswerOption a21 = new AnswerOption();
        a21.setText("Paris");
        a21.setCorrect(true);
        a21.setQuestion(q2);
        q2.getAnswerOptions().add(a21);

        AnswerOption a22 = new AnswerOption();
        a22.setText("Lyon");
        a22.setCorrect(false);
        a22.setQuestion(q2);
        q2.getAnswerOptions().add(a22);

        q2 = questionRepository.save(q2);

        mockMvc.perform(get("/api/quizzes/" + quiz.getId() + "/questions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                // Verify question texts
                .andExpect(jsonPath("$[0].text").value("What is 2+2?"))
                .andExpect(jsonPath("$[1].text").value("Capital of France?"))
                // Verify answer options exist and include expected entries
                .andExpect(jsonPath("$[0].answerOptions").isArray())
                .andExpect(jsonPath("$[0].answerOptions.length()").value(2))
                .andExpect(jsonPath("$[0].answerOptions[?(@.text=='4' && @.correct==true)]").isNotEmpty())
                .andExpect(jsonPath("$[1].answerOptions").isArray())
                .andExpect(jsonPath("$[1].answerOptions.length()").value(2))
                .andExpect(jsonPath("$[1].answerOptions[?(@.text=='Paris' && @.correct==true)]").isNotEmpty());
    }

    @Test
    void getQuestionsByQuizIdReturnsErrorWhenQuizDoesNotExist() throws Exception {
        long nonExistingId = (quiz.getId() != null ? quiz.getId() : 0L) + 9999L;
        mockMvc.perform(get("/api/quizzes/" + nonExistingId + "/questions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
