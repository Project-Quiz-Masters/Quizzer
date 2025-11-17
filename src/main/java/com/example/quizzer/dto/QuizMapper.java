package com.example.quizzer.dto;

import java.util.List;

import com.example.quizzer.quiz.Quiz;
import com.example.quizzer.question.Question;
import com.example.quizzer.answeroption.AnswerOption;

public class QuizMapper {

    public static QuizDTO toDTO(Quiz quiz) {
        return new QuizDTO(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getQuestions().stream()
                        .map(QuizMapper::toQuestionDTO)
                        .toList()
        );
    }

    private static QuestionDTO toQuestionDTO(Question q) {

        List<String> options = q.getAnswerOptions().stream()
                .map(AnswerOption::getText)
                .toList();

        List<String> correct = q.getAnswerOptions().stream()
                .filter(AnswerOption::isCorrect)
                .map(AnswerOption::getText)
                .toList();

        return new QuestionDTO(
                q.getId(),
                q.getText(),
                options,
                correct
        );
    }
}
