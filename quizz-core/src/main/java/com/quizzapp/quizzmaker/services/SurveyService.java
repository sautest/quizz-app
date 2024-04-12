package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;

import java.util.List;
import java.util.Optional;

public interface SurveyService {
    Survey createSurvey(SurveyDTO surveyDTO);
    Survey updateSurvey(SurveyDTO surveyDTO);
    List<Survey> getAllUserSurveys(Long id);

    List<Survey> getAllPublicSurveys();

    Optional<Survey> getSurvey(Long id);


    List<Survey> getAllQuestions(Long id);


    List<Survey> getAllQuestionsByUuid(String uuid);
    void deleteSurvey(Long id);

}
