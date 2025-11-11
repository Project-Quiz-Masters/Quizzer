package com.example.quizzer.question;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public Question createQuestion(String text, String difficulty, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));

        Question question = new Question();
        question.setText(text);
        question.setDifficulty(difficulty != null ? difficulty : "Normal");
        question.setQuiz(quiz);

        return questionRepository.save(question);
    }

}
