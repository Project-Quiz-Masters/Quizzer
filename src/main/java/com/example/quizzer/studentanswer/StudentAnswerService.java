package com.example.quizzer.studentanswer;

import com.example.quizzer.answeroption.AnswerOption;
import com.example.quizzer.answeroption.AnswerOptionRepository;
import com.example.quizzer.question.Question;
import com.example.quizzer.dto.QuestionResultDTO;
import com.example.quizzer.dto.QuizSubmissionDTO;
import com.example.quizzer.dto.QuizSubmissionResultDTO;
import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.quiz.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

@Service
public class StudentAnswerService {

    private final StudentAnswerRepository studentAnswerRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private QuizRepository quizRepository;

    public StudentAnswerService(StudentAnswerRepository studentAnswerRepository,
            AnswerOptionRepository answerOptionRepository,
            QuizRepository quizRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.quizRepository = quizRepository;
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
    // Service method for the quiz results

    public List<QuestionResultDTO> getQuizResults(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        List<QuestionResultDTO> results = new ArrayList<>();

        for (Question question : quiz.getQuestions()) {
            QuestionResultDTO dto = new QuestionResultDTO();
            dto.setQuestionId(question.getId());
            dto.setQuestionText(question.getText());
            dto.setQuestionDifficulty(question.getDifficulty());

            long totalAnswers = studentAnswerRepository.countByAnswerQuestionId(question.getId());
            long correctAnswers = studentAnswerRepository
                    .countByAnswerQuestionIdAndAnswerIsCorrectTrue(question.getId());
            long wrongAnswers = totalAnswers - correctAnswers;

            dto.setTotalAnswers(totalAnswers);
            dto.setCorrectAnswers(correctAnswers);
            dto.setWrongAnswers(wrongAnswers);

            results.add(dto);
        }

        return results;
    }

    @Transactional
    public QuizSubmissionResultDTO submitQuiz(QuizSubmissionDTO dto) {

    int correctCount = 0;

    for (QuizSubmissionDTO.AnswerSubmission a : dto.getAnswers()) {

        AnswerOption option = answerOptionRepository.findById(a.getAnswerOptionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Answer option not found"));

        // Save the student answer (anonymous)
        StudentAnswer sa = new StudentAnswer();
        sa.setAnswer(option);
        studentAnswerRepository.save(sa);

        // Count correct answers
        if (option.isCorrect()) {
            correctCount++;
        }
    }

    QuizSubmissionResultDTO result = new QuizSubmissionResultDTO();
    result.setQuizId(dto.getQuizId());
    result.setCorrectCount(correctCount);
    result.setTotalQuestions(dto.getAnswers().size());

    return result;
}

}
