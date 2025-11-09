package com.example.quizzer.quiz;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public Quiz createQuiz(Quiz quiz) {
        if (quiz.getCreatedAt() == null) {
            quiz.setCreatedAt(LocalDateTime.now());
        }
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        return quizRepository.findById(id).map(existing -> {
            existing.setTitle(updatedQuiz.getTitle());
            existing.setDescription(updatedQuiz.getDescription());
            existing.setCourse(updatedQuiz.getCourse());
            existing.setPublished(updatedQuiz.isPublished());
            existing.setTeacherId(updatedQuiz.getTeacherId());
            return quizRepository.save(existing);
        }).orElse(null);
    }

    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}
