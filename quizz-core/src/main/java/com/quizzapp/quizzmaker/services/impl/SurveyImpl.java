package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.models.ProjectStatus;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.SurveyRepository;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SurveyImpl implements SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Survey createSurvey(SurveyDTO surveyDTO) {
        User user = userRepository.findById(surveyDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Survey survey = new Survey(surveyDTO.getId(), UUID.randomUUID().toString().substring(0, 36), surveyDTO.getTitle(), ProjectStatus.IN_DESIGN,
                0, surveyDTO.getDateCreated(), user, new ArrayList<>(), surveyDTO.getSettings(), surveyDTO.getTheme());
        user.getSurvey().add(survey);
        userRepository.save((user));

        return survey;
    }

    @Override
    public Survey updateSurvey(SurveyDTO surveyDTO) {
        Survey survey = surveyRepository.findById((long) surveyDTO.getId()).orElseThrow(() -> new RuntimeException("Survey not found"));
        survey.setTitle(surveyDTO.getTitle());
        survey.setStatus(surveyDTO.getStatus());
        survey.setQuestions(surveyDTO.getQuestions());
        survey.setSettings(surveyDTO.getSettings());
        survey.setTheme(surveyDTO.getTheme());
        survey.setResponses(surveyDTO.getResponses());
        return surveyRepository.save(survey);
    }

    @Override
    public List<Survey> getAllUserSurveys(Long id) {
        {
            return surveyRepository.findByUserId(id);
        }
    }

    @Override
    public List<Survey> getAllPublicSurveys() {
        return surveyRepository.findAll();
    }

    @Override
    public Optional<Survey> getSurvey(Long id) {
        Optional<Survey> survey = surveyRepository.findById(id);
        return survey;
    }


    @Override
    public List<Survey> getAllQuestions(Long id) {
        return surveyRepository.findAllById(id);
    }

    @Override
    public List<Survey> getAllQuestionsByUuid(String uuid) {
        return surveyRepository.findAllByUuid(uuid);
    }

    @Override
    public void deleteSurvey(Long id) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with id: " + id));

        surveyRepository.delete(survey);
    }


}
