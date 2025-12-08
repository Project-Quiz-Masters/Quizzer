package com.example.quizzer.studentanswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    
    @Query("SELECT COUNT(sa) FROM StudentAnswer sa WHERE sa.answer.question.id = :questionId")
    long countByAnswerQuestionId(@Param("questionId") Long questionId);
    
    @Query("SELECT COUNT(sa) FROM StudentAnswer sa WHERE sa.answer.question.id = :questionId AND sa.answer.correct = true")
    long countByAnswerQuestionIdAndAnswerIsCorrectTrue(@Param("questionId") Long questionId);
    
    @Query("SELECT COUNT(sa) FROM StudentAnswer sa WHERE sa.answer.question.quiz.id = :quizId")
    long countByQuizId(@Param("quizId") Long quizId);
}
