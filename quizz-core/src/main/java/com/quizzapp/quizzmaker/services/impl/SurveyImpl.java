package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.SurveyRepository;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Survey createSurvey(SurveyDTO surveyDTO) {
        User user = userRepository.findById(surveyDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Survey survey = new Survey(surveyDTO.getId(), surveyDTO.getTitle(), user,new ArrayList<>(),surveyDTO.getSettings());
        user.getSurvey().add(survey);
        userRepository.save((user));

        return survey;
    }

    @Override
    public Survey updateSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyRepository.findById((long)surveyDTO.getId()).orElseThrow(() -> new RuntimeException("Survey not found"));
        survey.setTitle(surveyDTO.getTitle());
        survey.setQuestions(surveyDTO.getQuestions());
        survey.setSettings(surveyDTO.getSettings());
        return surveyRepository.save(survey);
    }

    @Override
    public List<Survey> getAllUserSurveys(Long id) {
        {
            return surveyRepository.findByUserId(id);
        }
    }

    @Override
    public List<Survey> getAllQuestions(Long id) {
        return surveyRepository.findAllById(id);
    }
}
