package com.example.quizzer.model;

/**
 * DifficultyLevel Enum
 * 
 * USER STORY 5: Add question to a quiz
 * TASK #12: Design "Add Question" form with difficulty options
 * TASK #13: Implement Question entity with difficulty level
 * 
 * Defines three difficulty levels for questions: EASY, NORMAL, and HARD.
 * Default difficulty level for new questions is NORMAL.
 */
public enum DifficultyLevel {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard");
    
    private final String displayName;
    
    DifficultyLevel(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
