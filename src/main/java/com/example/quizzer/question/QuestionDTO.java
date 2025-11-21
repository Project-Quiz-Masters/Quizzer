package com.example.quizzer.question;

public class QuestionDTO {
    private Long id;
    private String text;
    private String difficulty;

    public QuestionDTO() {}

    public QuestionDTO(Long id, String text, String difficulty) {
        this.id = id;
        this.text = text;
        this.difficulty = difficulty;
    }

    // Factory method to create DTO from Question entity
    public static QuestionDTO from(Question question) {
        return new QuestionDTO(
            question.getId(),
            question.getText(),
            question.getDifficulty()
        );
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
