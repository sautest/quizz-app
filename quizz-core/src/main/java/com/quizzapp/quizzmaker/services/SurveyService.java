package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;

import java.util.List;

public interface SurveyService {
    Survey createSurvey(SurveyDTO surveyDTO);
    Survey updateSurvey(SurveyDTO surveyDTO);
    List<Survey> getAllUserSurveys(Long id);

    List<Survey> getAllSurveys();

    List<Survey> getAllQuestions(Long id);


    List<Survey> getAllQuestionsByUuid(String uuid);
    void deleteSurvey(Long id);

}
