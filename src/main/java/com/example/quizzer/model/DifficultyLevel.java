package com.example.quizzer.model;

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
