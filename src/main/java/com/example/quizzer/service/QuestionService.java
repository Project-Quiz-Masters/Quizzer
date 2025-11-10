package com.example.quizzer.service;

import com.example.quizzer.model.Question;
import com.example.quizzer.model.Quiz;
import com.example.quizzer.repository.QuestionRepository;
import com.example.quizzer.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    /**
     * Create a new question for a quiz
     */
    public Question createQuestion(String text, String difficulty, Long quizId) {
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        
        if (quiz.isEmpty()) {
            throw new IllegalArgumentException("Quiz with ID " + quizId + " not found");
        }
        
        Question question = new Question();
        question.setText(text);
        question.setDifficulty(com.example.quizzer.model.DifficultyLevel.valueOf(difficulty));
        question.setQuiz(quiz.get());
        
        return questionRepository.save(question);
    }
    
    /**
     * Get all questions for a specific quiz
     */
    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
    
    /**
     * Get a question by ID
     */
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }
    
    /**
     * Update an existing question
     */
    public Question updateQuestion(Long id, String text, String difficulty) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        
        if (existingQuestion.isEmpty()) {
            throw new IllegalArgumentException("Question with ID " + id + " not found");
        }
        
        Question question = existingQuestion.get();
        question.setText(text);
        question.setDifficulty(com.example.quizzer.model.DifficultyLevel.valueOf(difficulty));
        
        return questionRepository.save(question);
    }
    
    /**
     * Delete a question
     */
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new IllegalArgumentException("Question with ID " + id + " not found");
        }
        questionRepository.deleteById(id);
    }
    
    /**
     * Get all questions
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
