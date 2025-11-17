package com.example.quizzer.quiz;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTeacherId(Long teacherId);

    List<Quiz> findByTeacherIdAndPublishedTrue(Long teacherId);

    List<Quiz> findByPublishedTrue(); // for ex. 20

    // Find quizzes that belong to a category (by category id)
    List<Quiz> findByCategoryId(Long categoryId);

}
