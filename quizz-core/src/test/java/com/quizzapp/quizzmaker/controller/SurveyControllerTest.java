package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.QuizService;
import com.quizzapp.quizzmaker.services.SurveyService;
import com.quizzapp.quizzmaker.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SurveyControllerTest {

    private MockMvc mockMvc;


    @Mock
    private SurveyService surveyService;
    @Mock
    private UserService userService;
    @Mock
    private GraphService graphService;

    @InjectMocks
    private SurveyController surveyController;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(surveyController).build();
    }

    @Test
    public void testGetAllPublicSurveys() {

        List<Survey> expectedSurveys = new ArrayList<>();
        when(surveyService.getAllPublicSurveys()).thenReturn(expectedSurveys);
        List<Survey> actualQuizzes = surveyController.getAllPublicSurveys();
        assertEquals(expectedSurveys, actualQuizzes, "The returned list of quizzes should match the expected list.");
    }


    @Test
    public void testGetAllQuestionsByUuid() throws Exception {
        String uuid = "111";

        List<Survey> expectedSurveys = new ArrayList<>();

        when(surveyService.getAllQuestionsByUuid(uuid)).thenReturn(expectedSurveys);

        mockMvc.perform(get("/api/survey/{uuid}/questions/uuid", uuid))
                .andExpect(status().isOk());
    }


    @Test
    public void testCreateSurvey() {
        SurveyDTO surveyDTO = new SurveyDTO();
        Survey expectedSurvey = new Survey();

        when(surveyService.createSurvey(any(SurveyDTO.class))).thenReturn(expectedSurvey);

        Survey resultSurvey = surveyController.createSurvey(surveyDTO);

        assertEquals(expectedSurvey, resultSurvey, "The returned quiz should match the expected quiz");

        verify(surveyService).createSurvey(any(SurveyDTO.class));
    }


    @Test
    public void testGenerateSurveyGraph() {
        GraphDTO graphDTO = new GraphDTO();
        String expectedGraphData = "generatedGraphData";

        when(graphService.generate(any(GraphDTO.class))).thenReturn(expectedGraphData);

        ResponseEntity<Map<String, String>> responseEntity = surveyController.generateQuizGraph(graphDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "The response status should be OK");
        assertEquals(expectedGraphData, responseEntity.getBody().get("graph"), "The graph data in the response should match the expected graph data");

        verify(graphService).generate(any(GraphDTO.class));
    }


}
