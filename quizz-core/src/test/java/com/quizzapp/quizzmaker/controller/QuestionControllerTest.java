package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;
public class QuestionControllerTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuestions() {
        Long userId = 1L;
        List<Question> questions = List.of(new Question(), new Question());
        when(questionService.getAllUserQuestions(userId)).thenReturn(questions);

        List<Question> result = questionController.getQuestions(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(questionService).getAllUserQuestions(userId);
    }

}
