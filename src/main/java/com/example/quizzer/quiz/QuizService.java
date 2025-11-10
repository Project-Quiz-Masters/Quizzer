package com.example.quizzer.quiz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

    public List<Quiz> getQuizzesByTeacher(Long teacherId) {
        return quizRepository.findByTeacherId(teacherId);
    }

  // (sp1-us-4 DELETE FUNCTIONALITY
    public boolean deleteQuiz(Long quizId, Long teacherId) {
        Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
        
        if (optionalQuiz.isPresent()) {
            Quiz quiz = optionalQuiz.get();
            
            // Security check: only the teacher who created the quiz can delete it
            if (!quiz.getTeacherId().equals(teacherId)) {
                return false; // Unauthorized deletion attempt
            }
            
            // (sp1-us-4)The cascade configuration in Quiz entity should handle question deletion
            quizRepository.delete(quiz);
            return true;
        }
        return false; // Quiz not found
    }
    
    // (sp1-us-4) Overloaded method without teacher check (for admin purposes)
    public boolean deleteQuiz(Long quizId) {
        if (quizRepository.existsById(quizId)) {
            quizRepository.deleteById(quizId);
            return true;
        }
        return false;
    }
}

