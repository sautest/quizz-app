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
        // Arrange
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(0); // Set a placeholder ID since it will be generated after saving
        surveyDTO.setUserId(1); // Set the user ID
        surveyDTO.setTitle("Sample Survey"); // Set the title of the survey
        surveyDTO.setResponses(0); // Initialize responses to 0
        surveyDTO.setDateCreated("2024-03-20"); // Set the creation date
        surveyDTO.setStatus(ProjectStatus.IN_DESIGN); // Set the status to IN_DESIGN
        surveyDTO.setSettings(new Settings()); // Assuming Settings is initialized properly
        surveyDTO.setTheme(new Theme()); // Assuming Theme is initialized properly
        surveyDTO.setQuestions(new ArrayList<>());
        surveyDTO.setUserId(1); // Set a valid user ID for the survey DTO

        User user = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(surveyDTO.getUserId())).thenReturn(Optional.of(user));
        when(surveyRepository.save(any(Survey.class))).thenAnswer(invocation -> {
            Survey savedSurvey = invocation.getArgument(0);
            savedSurvey.setId(1); // Set a valid ID for the saved survey
            return savedSurvey;
        });

        // Act
        Survey result = surveyService.createSurvey(surveyDTO);

        // Assert
        assertNotNull(result, "The created survey should not be null");
        assertNotNull(result.getId(), "The ID of the created survey should not be null");
        assertEquals(surveyDTO.getTitle(), result.getTitle(), "The titles should match");
        assertEquals(0, result.getResponses(), "The responses of the created survey should be initialized to 0");
        assertNotNull(result.getDateCreated(), "The date created of the created survey should not be null");
        assertEquals(ProjectStatus.IN_DESIGN, result.getStatus(), "The status of the created survey should be IN_DESIGN");
        assertEquals(user, result.getUser(), "The user of the created survey should match the provided user");
        assertEquals(surveyDTO.getSettings(), result.getSettings(), "The settings of the created survey should match");
        assertEquals(surveyDTO.getTheme(), result.getTheme(), "The theme of the created survey should match");

        // Verify interactions
        verify(userRepository, times(1)).findById(surveyDTO.getUserId());
    }

    @Test
    void updateSurvey_Successful() {
        // Arrange
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setId(1); // Set the ID of the survey to be updated
        surveyDTO.setTitle("Updated Survey Title");
        surveyDTO.setStatus(ProjectStatus.IN_DESIGN);
        surveyDTO.setQuestions(null); // Assuming questions are not being updated in this test
        surveyDTO.setSettings(new Settings()); // Assuming Settings is initialized properly
        surveyDTO.setTheme(new Theme()); // Assuming Theme is initialized properly
        surveyDTO.setResponses(10); // Updated number of responses

        Survey existingSurvey = new Survey(); // Create an existing survey object with ID 1
        existingSurvey.setId(1);
        existingSurvey.setTitle("Original Survey Title");
        existingSurvey.setStatus(ProjectStatus.IN_DESIGN);
        existingSurvey.setSettings(new Settings()); // Assuming Settings is initialized properly
        existingSurvey.setTheme(new Theme()); // Assuming Theme is initialized properly
        existingSurvey.setResponses(5);

        when(surveyRepository.findById(1L)).thenReturn(Optional.of(existingSurvey));
        when(surveyRepository.save(any(Survey.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Survey updatedSurvey = surveyService.updateSurvey(surveyDTO);

        // Assert
        assertNotNull(updatedSurvey, "The updated survey should not be null");
        assertEquals(surveyDTO.getId(), updatedSurvey.getId(), "The IDs should match");
        assertEquals(surveyDTO.getTitle(), updatedSurvey.getTitle(), "The titles should match");
        assertEquals(surveyDTO.getStatus(), updatedSurvey.getStatus(), "The status should match");
        assertEquals(surveyDTO.getQuestions(), updatedSurvey.getQuestions(), "The questions should match");
        assertEquals(surveyDTO.getSettings(), updatedSurvey.getSettings(), "The settings should match");
        assertEquals(surveyDTO.getTheme(), updatedSurvey.getTheme(), "The theme should match");
        assertEquals(surveyDTO.getResponses(), updatedSurvey.getResponses(), "The number of responses should match");

        // Verify interactions
        verify(surveyRepository, times(1)).findById(1L);
        verify(surveyRepository, times(1)).save(any(Survey.class));
    }

    @Test
    void getAllUserSurveys_Successful() {
        // Arrange
        long userId = 1;
        List<Survey> userSurveys = new ArrayList<>(); // Assuming some surveys are retrieved for the user
        when(surveyRepository.findByUserId(userId)).thenReturn(userSurveys);

        // Act
        List<Survey> result = surveyService.getAllUserSurveys(userId);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(userSurveys, result, "The returned surveys should match the user surveys");

        // Verify interaction
        verify(surveyRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAllPublicSurveys_Successful() {
        // Arrange
        List<Survey> publicSurveys = new ArrayList<>(); // Assuming some public surveys are retrieved
        when(surveyRepository.findAll()).thenReturn(publicSurveys);

        // Act
        List<Survey> result = surveyService.getAllPublicSurveys();

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(publicSurveys, result, "The returned surveys should match the public surveys");

        // Verify interaction
        verify(surveyRepository, times(1)).findAll();
    }

    @Test
    void getSurvey_WithValidId_Successful() {
        // Arrange
        long surveyId = 1;
        Survey survey = new Survey(); // Assuming a survey with ID 1 is retrieved
        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(survey));

        // Act
        Optional<Survey> result = surveyService.getSurvey(surveyId);

        // Assert
        assertTrue(result.isPresent(), "The result should be present");
        assertEquals(survey, result.get(), "The returned survey should match the expected survey");

        // Verify interaction
        verify(surveyRepository, times(1)).findById(surveyId);
    }

    @Test
    void getAllQuestions_Successful() {
        // Arrange
        long id = 1;
        List<Survey> surveys = new ArrayList<>(); // Assuming some surveys are retrieved by ID
        when(surveyRepository.findAllById(id)).thenReturn(surveys);

        // Act
        List<Survey> result = surveyService.getAllQuestions(id);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(surveys, result, "The returned surveys should match the surveys by ID");

        // Verify interaction
        verify(surveyRepository, times(1)).findAllById(id);
    }

    @Test
    void getAllQuestionsByUuid_Successful() {
        // Arrange
        String uuid = "example_uuid";
        List<Survey> surveys = new ArrayList<>(); // Assuming some surveys are retrieved by UUID
        when(surveyRepository.findAllByUuid(uuid)).thenReturn(surveys);

        // Act
        List<Survey> result = surveyService.getAllQuestionsByUuid(uuid);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(surveys, result, "The returned surveys should match the surveys by UUID");

        // Verify interaction
        verify(surveyRepository, times(1)).findAllByUuid(uuid);
    }

    @Test
    void deleteSurvey_WithValidId_Successful() {
        // Arrange
        long id = 1;
        Survey survey = new Survey(); // Assuming a survey with ID 1 is retrieved
        when(surveyRepository.findById(id)).thenReturn(Optional.of(survey));

        // Act
        surveyService.deleteSurvey(id);

        // Verify interaction
        verify(surveyRepository, times(1)).findById(id);
        verify(surveyRepository, times(1)).delete(survey);
    }

    @Test
    void deleteSurvey_WithInvalidId_ExceptionThrown() {
        // Arrange
        long id = 1;
        when(surveyRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> surveyService.deleteSurvey(id));
    }


}
