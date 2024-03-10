package com.quizzapp.quizzmaker.persistence.repositories;

import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository   extends JpaRepository<Survey,Long> {

    @Query("SELECT e FROM Survey e WHERE user.id = :userId")
    List<Survey> findByUserId(Long userId);

    List<Survey> findAllById(Long surveyId);

    List<Survey> findAllByUuid(String surveyId);
}

