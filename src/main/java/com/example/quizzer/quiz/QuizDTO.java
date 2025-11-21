package com.example.quizzer.quiz;

public class QuizDTO {
    private Long id;
    private String name;
    private String description;
    private String courseCode;
    private String categoryName;
    private String createdAt;
    private boolean published;

    public QuizDTO() {}

    public QuizDTO(Long id, String name, String description, String courseCode, 
                   String categoryName, String createdAt, boolean published) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseCode = courseCode;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.published = published;
    }

    // Factory method to create DTO from Quiz entity
    public static QuizDTO from(Quiz quiz) {
        String formattedDate = quiz.getCreatedAt() != null 
            ? quiz.getCreatedAt().toString()
            : "";
        
        return new QuizDTO(
            quiz.getId(),
            quiz.getTitle(),
            quiz.getDescription(),
            quiz.getCourse(),
            "-", // categoryName - placeholder
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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
