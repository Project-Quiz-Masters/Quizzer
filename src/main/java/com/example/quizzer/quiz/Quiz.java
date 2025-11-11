package com.example.quizzer.quiz;

import java.time.LocalDateTime;
import java.util.List;

import com.example.quizzer.question.Question;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String course;
    private boolean published;
    private LocalDateTime createdAt;
    private Long teacherId; // Changed from String to Long
    
    // This cascade configuration ensures questions are deleted when quiz is deleted
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
    
    // Constructors
    public Quiz() {}
    
    public Quiz(String title, String description, String course, Long teacherId) {
        this.title = title;
        this.description = description;
        this.course = course;
        this.teacherId = teacherId;
        this.published = false;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getTeacherId() { return teacherId; } // Changed to Long
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; } // Changed to Long

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}