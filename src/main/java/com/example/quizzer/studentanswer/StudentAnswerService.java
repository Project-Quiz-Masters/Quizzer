package com.example.quizzer.studentanswer;

import com.example.quizzer.answeroption.AnswerOption;
import com.example.quizzer.answeroption.AnswerOptionRepository;
import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;
    private final AnswerOptionRepository answerOptionRepository;

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository,
            AnswerOptionRepository answerOptionRepository,
            QuizRepository quizRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.answerOptionRepository = answerOptionRepository;
    }

    @Transactional
    public StudentAnswer createStudentAnswer(Long answerOptionId) {
        // 1. Validate input
        if (answerOptionId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Answer option id must be provided");
        }

        // 2. Find the AnswerOption
        AnswerOption answerOption = answerOptionRepository.findById(answerOptionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer option not found"));

        // 3. Check if the quiz is published
        Quiz quiz = answerOption.getQuestion().getQuiz();
        if (!quiz.isPublished()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz is not published");
        }

        // 4. Create and save the StudentAnswer
        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setAnswer(answerOption);
        return studentAnswerRepository.save(studentAnswer);
    }
}