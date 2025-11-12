package com.example.quizzer.question;

import org.springframework.stereotype.Service;
import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Question addQuestion(Question question) {
        if (question.getQuiz() == null) {
            Quiz defaultQuiz = quizRepository.findAll()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No quiz found in database!"));
            question.setQuiz(defaultQuiz);
        }
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
