package com.example.quizzer.dto;

import java.util.List;

public class QuizSubmissionDTO {

    private Long quizId;
    private List<AnswerSubmission> answers;

    public static class AnswerSubmission {
        private Long questionId;
        private Long answerOptionId;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public Long getAnswerOptionId() {
            return answerOptionId;
        }

        public void setAnswerOptionId(Long answerOptionId) {
            this.answerOptionId = answerOptionId;
        }
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public List<AnswerSubmission> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerSubmission> answers) {
        this.answers = answers;
    }
}
