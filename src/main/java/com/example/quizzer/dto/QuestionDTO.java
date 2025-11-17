package com.example.quizzer.dto;

import java.util.List;

public record QuestionDTO(
        Long id,
        String text,
        List<String> options,
        List<String> correct
) {}
