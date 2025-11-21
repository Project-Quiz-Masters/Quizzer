package com.example.quizzer.quiz;

public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private String course;
    private String categoryName;
    private String createdAt;
    private boolean published;

    public QuizDTO() {}

    public QuizDTO(Long id, String title, String description, String course, 
                   String categoryName, String createdAt, boolean published) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.course = course;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.published = published;
    }

    // Factory method to create DTO from Quiz entity
    public static QuizDTO from(Quiz quiz) {
        String formattedDate = quiz.getCreatedAt() != null 
            ? quiz.getCreatedAt().toString()
            : "";
        
        String categoryName = "-";
        if (quiz.getCategory() != null && quiz.getCategory().getName() != null) {
            categoryName = quiz.getCategory().getName();
        }

        return new QuizDTO(
            quiz.getId(),
            quiz.getTitle(),
            quiz.getDescription(),
            quiz.getCourse(),
            categoryName,
            formattedDate,
            quiz.isPublished()
        );
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
