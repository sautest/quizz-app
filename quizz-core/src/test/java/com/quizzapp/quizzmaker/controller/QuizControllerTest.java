package com.quizzapp.quizzmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.QuizService;
import com.quizzapp.quizzmaker.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;
public class QuizControllerTest {
    private MockMvc mockMvc;


    @Mock
    private QuizService quizService;
    @Mock
    private UserService userService;
    @Mock
    private GraphService graphService;

    @InjectMocks
    private QuizController quizController;

    private User user;
    private Authentication authentication;
    private SecurityContext securityContext;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(quizController).build();

        user = new User(1, "user@test.com", "testUser", "password123", false, "2023-01-01", "ROLE_USER", null, null); // Assume User has an appropriate constructor
        authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        securityContext = mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    public void testGetAllPublicQuizzes() {

        List<Quiz> expectedQuizzes = new ArrayList<>();
        when(quizService.getAllPublicQuizzes()).thenReturn(expectedQuizzes);
        List<Quiz> actualQuizzes = quizController.getAllPublicQuizzes();
        assertEquals(expectedQuizzes, actualQuizzes, "The returned list of quizzes should match the expected list.");
    }


    @Test
    public void testGetAllQuestionsByUuid() throws Exception {
        String uuid = "111";

        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizService.getAllQuestionsByUuid(uuid)).thenReturn(expectedQuizzes);

        mockMvc.perform(get("/api/quiz/{uuid}/questions/uuid", uuid))
                .andExpect(status().isOk());
    }


    @Test
    public void testCreateQuiz() {
        QuizDTO quizDTO = new QuizDTO();
        Quiz expectedQuiz = new Quiz();

        when(quizService.createQuiz(any(QuizDTO.class))).thenReturn(expectedQuiz);

        Quiz resultQuiz = quizController.createQuiz(quizDTO);

        assertEquals(expectedQuiz, resultQuiz, "The returned quiz should match the expected quiz");

        verify(quizService).createQuiz(any(QuizDTO.class));
    }


    @Test
    public void testGenerateQuizGraph() {
        GraphDTO graphDTO = new GraphDTO();
        String expectedGraphData = "generatedGraphData";

        when(graphService.generate(any(GraphDTO.class))).thenReturn(expectedGraphData);

        ResponseEntity<Map<String, String>> responseEntity = quizController.generateQuizGraph(graphDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "The response status should be OK");
        assertEquals(expectedGraphData, responseEntity.getBody().get("graph"), "The graph data in the response should match the expected graph data");

        verify(graphService).generate(any(GraphDTO.class));
    }

}
