package com.example.quizzer.answeroption;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnswerOptionService {
    
    private final AnswerOptionRepository answerOptionRepository;

    public AnswerOptionService(AnswerOptionRepository answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }

    public List<AnswerOption> getAnswerOptionsByQuestion(Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }

    public AnswerOption addAnswerOption(AnswerOption answerOption) {
        return answerOptionRepository.save(answerOption);
    }

    public void deleteAnswerOption(Long id) {
        answerOptionRepository.deleteById(id);
    }
}
