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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(0);
        surveyDTO.setUserId(1);
        surveyDTO.setTitle("bla bla");
        surveyDTO.setResponses(0);
        surveyDTO.setDateCreated("2024-03-20");
        surveyDTO.setStatus(ProjectStatus.IN_DESIGN);
        surveyDTO.setSettings(new Settings());
        surveyDTO.setTheme(new Theme());
        surveyDTO.setQuestions(new ArrayList<>());
        surveyDTO.setUserId(1);

        User user = new User(1, "user1@gmail.com", "user1", "password", false, "2024-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(surveyDTO.getUserId())).thenReturn(Optional.of(user));
        when(surveyRepository.save(any(Survey.class))).thenAnswer(invocation -> {
            Survey savedSurvey = invocation.getArgument(0);
            savedSurvey.setId(1);
            return savedSurvey;
        });

        Survey result = surveyService.createSurvey(surveyDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(surveyDTO.getTitle(), result.getTitle());
        assertEquals(0, result.getResponses());
        assertNotNull(result.getDateCreated());
        assertEquals(ProjectStatus.IN_DESIGN, result.getStatus());
        assertEquals(user, result.getUser());
        assertEquals(surveyDTO.getSettings(), result.getSettings());
        assertEquals(surveyDTO.getTheme(), result.getTheme());

        verify(userRepository).findById(surveyDTO.getUserId());
    }

    @Test
    void testUpdateSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(1);
        surveyDTO.setTitle("bla bla1");
        surveyDTO.setStatus(ProjectStatus.IN_DESIGN);
        surveyDTO.setQuestions(null);
        surveyDTO.setSettings(new Settings());
        surveyDTO.setTheme(new Theme());
        surveyDTO.setResponses(10);

        Survey existingSurvey = new Survey();
        existingSurvey.setId(1);
        existingSurvey.setTitle("blab bla2");
        existingSurvey.setStatus(ProjectStatus.IN_DESIGN);
        existingSurvey.setSettings(new Settings());
        existingSurvey.setTheme(new Theme());
        existingSurvey.setResponses(5);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Survey updatedSurvey = surveyService.updateSurvey(surveyDTO);

        assertNotNull(updatedSurvey);
        assertEquals(surveyDTO.getId(), updatedSurvey.getId());
        assertEquals(surveyDTO.getTitle(), updatedSurvey.getTitle());
        assertEquals(surveyDTO.getStatus(), updatedSurvey.getStatus());
        assertEquals(surveyDTO.getQuestions(), updatedSurvey.getQuestions());
        assertEquals(surveyDTO.getSettings(), updatedSurvey.getSettings());
        assertEquals(surveyDTO.getTheme(), updatedSurvey.getTheme());
        assertEquals(surveyDTO.getResponses(), updatedSurvey.getResponses());

        verify(surveyRepository).findById(1L);
        verify(surveyRepository).save(any(Survey.class));
    }

    @Test
    void testGetAllUserSurveys() {
        long userId = 1;
        List<Survey> userSurveys = new ArrayList<>();
        when(surveyRepository.findByUserId(userId)).thenReturn(userSurveys);

        List<Survey> result = surveyService.getAllUserSurveys(userId);

        assertNotNull(result);
        assertEquals(userSurveys, result);

        verify(surveyRepository).findByUserId(userId);
    }

    @Test
    void testGetAllPublicSurveys() {
        List<Survey> publicSurveys = new ArrayList<>();
        when(surveyRepository.findAll()).thenReturn(publicSurveys);

        List<Survey> result = surveyService.getAllPublicSurveys();

        assertNotNull(result);
        assertEquals(publicSurveys, result);

        verify(surveyRepository).findAll();
    }

    @Test
    void testGetSurvey() {
        long surveyId = 1;
        Survey survey = new Survey();
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        Optional<Survey> result = surveyService.getSurvey(surveyId);

        assertTrue(result.isPresent());
        assertEquals(survey, result.get());

        verify(surveyRepository).findById(surveyId);
    }

    @Test
    void testGetAllQuestions() {
        long id = 1;
        List<Survey> surveys = new ArrayList<>();
        when(surveyRepository.findAllById(id)).thenReturn(surveys);

        List<Survey> result = surveyService.getAllQuestions(id);

        assertNotNull(result);
        assertEquals(surveys, result);

        verify(surveyRepository).findAllById(id);
    }

    @Test
    void testGetAllQuestionsByUuid() {
        String uuid = "4224242424";
        List<Survey> surveys = new ArrayList<>();
        when(surveyRepository.findAllByUuid(uuid)).thenReturn(surveys);

        List<Survey> result = surveyService.getAllQuestionsByUuid(uuid);

        assertNotNull(result);
        assertEquals(surveys, result);

        verify(surveyRepository).findAllByUuid(uuid);
    }

    @Test
    void testDeleteSurvey() {
        long id = 1;
        Survey survey = new Survey();
        when(surveyRepository.findById(id)).thenReturn(Optional.of(survey));

        surveyService.deleteSurvey(id);

        verify(surveyRepository).findById(id);
        verify(surveyRepository).delete(survey);
    }

    @Test
    void testDeleteSurveyException() {
        long id = 1;
        when(surveyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> surveyService.deleteSurvey(id));
    }


}
