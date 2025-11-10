package com.example.quizzer.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Quiz Entity
 * 
 * RELATED TO ALL USER STORIES 1-6:
 * - USER STORY 1: Create quiz
 * - USER STORY 2: View list of quizzes
 * - USER STORY 3: Edit quiz
 * - USER STORY 4: Delete quiz
 * - USER STORY 5: Add questions to quiz (TASK #13)
 * - USER STORY 6: View questions in quiz
 * 
 * Represents a quiz that contains multiple questions.
 * Teachers can create, edit, and delete quizzes, and manage questions within them.
 */
@Entity
@Table(name = "quizzes")
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();
    
    // Constructors
    public Quiz() {
    }
    
    public Quiz(String title, String description) {
        this.title = title;
        this.description = description;
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
    
    public List<Question> getQuestions() {
        return questions;
    }
    
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public void addQuestion(Question question) {
        questions.add(question);
        question.setQuiz(this);
    }
    
    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setQuiz(null);
    }
    
    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
