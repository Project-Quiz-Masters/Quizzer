package com.example.quizzer.dto;

public record AnswerOptionDTO(
        Long id,
        String text,
        boolean correct
) {}
