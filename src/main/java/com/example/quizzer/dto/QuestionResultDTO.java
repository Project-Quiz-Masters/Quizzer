package com.example.quizzer.dto;

public class QuestionResultDTO {
    private Long questionId;
    private String questionText;
    private String questionDifficulty;
    private Long totalAnswers;
    private Long correctAnswers;
    private Long wrongAnswers;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(String questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public Long getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Long totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public Long getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Long correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Long getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(Long wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
