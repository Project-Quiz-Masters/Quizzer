package com.example.quizzer.quiz;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;

    }

    public Quiz addQuiz(String title, String description, String course, String teacherId) {

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setCourse(course);
        quiz.setTeacherId(teacherId);
        quiz.setPublished(false);
        quiz.setCreatedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public List<Quiz> listQuizzes() {
        return quizRepository.findAll();
    }
}
