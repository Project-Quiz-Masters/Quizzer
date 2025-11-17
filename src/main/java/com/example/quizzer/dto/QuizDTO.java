package com.example.quizzer.dto;

import java.util.List;

public record QuizDTO(
        Long id,
        String title,
        String description,
        List<QuestionDTO> questions
) {}
