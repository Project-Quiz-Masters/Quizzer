package java.com.example.quizzer.studentanswer;

import com.example.quizzer.answeroption.AnswerOption;
import com.example.quizzer.answeroption.AnswerOptionRepository;
import com.example.quizzer.question.Question;
import com.example.quizzer.question.QuestionRepository;
import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import com.example.quizzer.studentanswer.StudentAnswerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentAnswerRestControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerOptionRepository answerOptionRepository;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;

    @BeforeEach
    void setUp() {
        studentAnswerRepository.deleteAll();
        answerOptionRepository.deleteAll();
        questionRepository.deleteAll();
        quizRepository.deleteAll();
    }

    // ---------------- Helpers ----------------
    private AnswerOption createPublishedQuizOption() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Published Quiz");
        quiz.setPublished(true);
        quiz = quizRepository.save(quiz);

        Question q = new Question();
        q.setQuiz(quiz);
        q.setText("Sample question");
        q = questionRepository.save(q);

        AnswerOption opt = new AnswerOption();
        opt.setQuestion(q);
        opt.setText("Option");
        opt.setCorrect(true);
        return answerOptionRepository.save(opt);
    }

    private AnswerOption createUnpublishedQuizOption() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Unpublished Quiz");
        quiz.setPublished(false);
        quiz = quizRepository.save(quiz);

        Question q = new Question();
        q.setQuiz(quiz);
        q.setText("Sample question");
        q = questionRepository.save(q);

        AnswerOption opt = new AnswerOption();
        opt.setQuestion(q);
        opt.setText("Option");
        opt.setCorrect(false);
        return answerOptionRepository.save(opt);
    }
}
