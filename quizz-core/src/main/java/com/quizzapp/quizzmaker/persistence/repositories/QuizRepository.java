package com.quizzapp.quizzmaker.persistence.repositories;

import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizRepository  extends JpaRepository<Quiz,Long> {
    @Query("SELECT e FROM Quiz e WHERE user.id = :userId")
    List<Quiz> findByUserId(Long userId);

    List<Quiz> findAllById(Long quizId);

    List<Quiz> findAllByUuid(String quizId);
}
