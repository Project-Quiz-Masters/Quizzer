package com.example.quizzer.model;

import jakarta.persistence.*;

/**
 * Question Entity
 * 
 * USER STORY 5: Add question to a quiz
 * TASK #13: Implement Question entity and repository
 * 
 * Represents a question within a quiz. Each question has text content,
 * a difficulty level (Easy, Normal, Hard), and belongs to a specific quiz.
 */
@Entity
@Table(name = "questions")
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    // Constructors
    public Question() {
        this.difficulty = DifficultyLevel.NORMAL;
    }
    
    public Question(String text, DifficultyLevel difficulty, Quiz quiz) {
        this.text = text;
        this.difficulty = difficulty;
        this.quiz = quiz;
    }
    
    public Question(String text, Quiz quiz) {
        this.text = text;
        this.difficulty = DifficultyLevel.NORMAL;
        this.quiz = quiz;
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
    
    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
    
    public Quiz getQuiz() {
        return quiz;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", difficulty=" + difficulty +
                ", quizId=" + (quiz != null ? quiz.getId() : "null") +
                '}';
    }
}
