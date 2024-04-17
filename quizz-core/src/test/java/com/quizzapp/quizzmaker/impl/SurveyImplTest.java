package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.repositories.SurveyRepository;
import com.quizzapp.quizzmaker.services.impl.SurveyImpl;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.models.ProjectStatus;
import com.quizzapp.quizzmaker.persistence.entities.Settings;
import com.quizzapp.quizzmaker.persistence.entities.Theme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SurveyImplTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SurveyImpl surveyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createSurvey_Successful() {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(0);
        surveyDTO.setUserId(1);
        surveyDTO.setTitle("Sample Survey");
        surveyDTO.setResponses(0);
        surveyDTO.setDateCreated("2024-03-20");
        surveyDTO.setStatus(ProjectStatus.IN_DESIGN);
        surveyDTO.setSettings(new Settings());
        surveyDTO.setTheme(new Theme());
        surveyDTO.setQuestions(new ArrayList<>());
        surveyDTO.setUserId(1);

        User user = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(surveyDTO.getUserId())).thenReturn(Optional.of(user));
        when(surveyRepository.save(any(Survey.class))).thenAnswer(invocation -> {
            Survey savedSurvey = invocation.getArgument(0);
            savedSurvey.setId(1);
            return savedSurvey;
        });

        Survey result = surveyService.createSurvey(surveyDTO);

        assertNotNull(result, "The created survey should not be null");
        assertNotNull(result.getId(), "The ID of the created survey should not be null");
        assertEquals(surveyDTO.getTitle(), result.getTitle(), "The titles should match");
        assertEquals(0, result.getResponses(), "The responses of the created survey should be initialized to 0");
        assertNotNull(result.getDateCreated(), "The date created of the created survey should not be null");
        assertEquals(ProjectStatus.IN_DESIGN, result.getStatus(), "The status of the created survey should be IN_DESIGN");
        assertEquals(user, result.getUser(), "The user of the created survey should match the provided user");
        assertEquals(surveyDTO.getSettings(), result.getSettings(), "The settings of the created survey should match");
        assertEquals(surveyDTO.getTheme(), result.getTheme(), "The theme of the created survey should match");

        verify(userRepository, times(1)).findById(surveyDTO.getUserId());
    }

    @Test
    void updateSurvey_Successful() {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(1);
        surveyDTO.setTitle("Updated Survey Title");
        surveyDTO.setStatus(ProjectStatus.IN_DESIGN);
        surveyDTO.setQuestions(null);
        surveyDTO.setSettings(new Settings());
        surveyDTO.setTheme(new Theme());
        surveyDTO.setResponses(10);

        Survey existingSurvey = new Survey();
        existingSurvey.setId(1);
        existingSurvey.setTitle("Original Survey Title");
        existingSurvey.setStatus(ProjectStatus.IN_DESIGN);
        existingSurvey.setSettings(new Settings());
        existingSurvey.setTheme(new Theme());
        existingSurvey.setResponses(5);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Survey updatedSurvey = surveyService.updateSurvey(surveyDTO);

        assertNotNull(updatedSurvey, "The updated survey should not be null");
        assertEquals(surveyDTO.getId(), updatedSurvey.getId(), "The IDs should match");
        assertEquals(surveyDTO.getTitle(), updatedSurvey.getTitle(), "The titles should match");
        assertEquals(surveyDTO.getStatus(), updatedSurvey.getStatus(), "The status should match");
        assertEquals(surveyDTO.getQuestions(), updatedSurvey.getQuestions(), "The questions should match");
        assertEquals(surveyDTO.getSettings(), updatedSurvey.getSettings(), "The settings should match");
        assertEquals(surveyDTO.getTheme(), updatedSurvey.getTheme(), "The theme should match");
        assertEquals(surveyDTO.getResponses(), updatedSurvey.getResponses(), "The number of responses should match");

        verify(surveyRepository, times(1)).findById(1L);
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    void getAllUserSurveys_Successful() {
        long userId = 1;
        List<Survey> userSurveys = new ArrayList<>();
        when(surveyRepository.findByUserId(userId)).thenReturn(userSurveys);

        List<Survey> result = surveyService.getAllUserSurveys(userId);

        assertNotNull(result, "The result should not be null");
        assertEquals(userSurveys, result, "The returned surveys should match the user surveys");

        verify(surveyRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAllPublicSurveys_Successful() {
        List<Survey> publicSurveys = new ArrayList<>();
        when(surveyRepository.findAll()).thenReturn(publicSurveys);

        List<Survey> result = surveyService.getAllPublicSurveys();

        assertNotNull(result, "The result should not be null");
        assertEquals(publicSurveys, result, "The returned surveys should match the public surveys");

        verify(surveyRepository, times(1)).findAll();
    }

    @Test
    void getSurvey_WithValidId_Successful() {
        long surveyId = 1;
        Survey survey = new Survey();
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        Optional<Survey> result = surveyService.getSurvey(surveyId);

        assertTrue(result.isPresent(), "The result should be present");
        assertEquals(survey, result.get(), "The returned survey should match the expected survey");

        verify(surveyRepository, times(1)).findById(surveyId);
    }

    @Test
    void getAllQuestions_Successful() {
        long id = 1;
        List<Survey> surveys = new ArrayList<>();
        when(surveyRepository.findAllById(id)).thenReturn(surveys);

        List<Survey> result = surveyService.getAllQuestions(id);

        assertNotNull(result, "The result should not be null");
        assertEquals(surveys, result, "The returned surveys should match the surveys by ID");

        verify(surveyRepository, times(1)).findAllById(id);
    }

    @Test
    void getAllQuestionsByUuid_Successful() {
        String uuid = "example_uuid";
        List<Survey> surveys = new ArrayList<>();
        when(surveyRepository.findAllByUuid(uuid)).thenReturn(surveys);

        List<Survey> result = surveyService.getAllQuestionsByUuid(uuid);

        assertNotNull(result, "The result should not be null");
        assertEquals(surveys, result, "The returned surveys should match the surveys by UUID");

        verify(surveyRepository, times(1)).findAllByUuid(uuid);
    }

    @Test
    void deleteSurvey_WithValidId_Successful() {
        long id = 1;
        Survey survey = new Survey();
        when(surveyRepository.findById(id)).thenReturn(Optional.of(survey));

        surveyService.deleteSurvey(id);

        verify(surveyRepository, times(1)).findById(id);
        verify(surveyRepository, times(1)).delete(survey);
    }

    @Test
    void deleteSurvey_WithInvalidId_ExceptionThrown() {
        long id = 1;
        when(surveyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> surveyService.deleteSurvey(id));
    }


}
