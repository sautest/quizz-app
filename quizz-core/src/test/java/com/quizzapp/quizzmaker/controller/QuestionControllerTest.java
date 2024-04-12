package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.services.QuestionService;
import com.quizzapp.quizzmaker.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
public class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getQuestions_Successful() {
        Long userId = 1L;
        List<Question> questions = List.of(new Question(), new Question());
        when(questionService.getAllUserQuestions(userId)).thenReturn(questions);

        List<Question> result = questionController.getQuestions(userId);

        assertNotNull(result, "The list of questions should not be null");
        assertEquals(2, result.size(), "The list should contain 2 questions");

        verify(questionService).getAllUserQuestions(userId);
    }


}
