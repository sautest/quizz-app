package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.*;
import com.quizzapp.quizzmaker.persistence.models.ProjectStatus;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.impl.QuizImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class QuizImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuizImpl quizService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateQuiz() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(0);
        quizDTO.setUserId(1);
        quizDTO.setTitle("Sample Quiz");
        quizDTO.setDateCreated("2024-03-20");
        quizDTO.setSettings(new Settings());
        quizDTO.setTheme(new Theme());

        User user = new User(1, "user1@gmail.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(quizDTO.getUserId())).thenReturn(Optional.of(user));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> {
            Quiz savedQuiz = invocation.getArgument(0);
            savedQuiz.setId(1);
            return savedQuiz;
        });

        Quiz result = quizService.createQuiz(quizDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(quizDTO.getTitle(), result.getTitle());
        assertNotNull(result.getDateCreated());
        assertEquals(user, result.getUser());
        assertEquals(quizDTO.getSettings(), result.getSettings());
        assertEquals(quizDTO.getTheme(), result.getTheme());

        verify(userRepository).findById(quizDTO.getUserId());
    }


    @Test
    void testUpdateQuiz() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(1);
        quizDTO.setTitle("title");
        quizDTO.setQuestions(new ArrayList<>()) ;
        quizDTO.setSettings(new Settings());
        quizDTO.setTheme(new Theme());
        quizDTO.setStatus(ProjectStatus.IN_DESIGN);
        quizDTO.setResponses(10);

        Quiz existingQuiz = new Quiz();

        when(quizRepository.findById((long) quizDTO.getId())).thenReturn(Optional.of(existingQuiz));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Quiz updatedQuiz = quizService.updateQuiz(quizDTO);

        assertNotNull(updatedQuiz);
        assertEquals(quizDTO.getTitle(), updatedQuiz.getTitle());
        assertEquals(quizDTO.getQuestions(), updatedQuiz.getQuestions());
        assertEquals(quizDTO.getSettings(), updatedQuiz.getSettings());
        assertEquals(quizDTO.getTheme(), updatedQuiz.getTheme());
        assertEquals(quizDTO.getStatus(), updatedQuiz.getStatus());
        assertEquals(quizDTO.getResponses(), updatedQuiz.getResponses());

        verify(quizRepository).findById((long) quizDTO.getId());
        verify(quizRepository).save(any(Quiz.class));
    }


    @Test
    void testGetAllUserQuizzes() {
        Long userId = 1L;
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findByUserId(userId)).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllUserQuizzes(userId);

        assertEquals(expectedQuizzes, result);

        verify(quizRepository).findByUserId(userId);
    }

    @Test
    void testGetAllPublicQuizzes() {
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findAll()).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllPublicQuizzes();

        assertEquals(expectedQuizzes, result);

        verify(quizRepository).findAll();
    }

    @Test
    void testGetQuiz() {
        Long quizId = 1L;
        Quiz expectedQuiz = new Quiz();

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(expectedQuiz));

        Optional<Quiz> result = quizService.getQuiz(quizId);

        assertEquals(Optional.of(expectedQuiz), result);

        verify(quizRepository).findById(quizId);
    }

    @Test
    void testGetAllQuestions() {
        Long quizId = 1L;
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findAllById(quizId)).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllQuestions(quizId);

        assertEquals(expectedQuizzes, result);

        verify(quizRepository).findAllById(quizId);
    }

    @Test
    void testGetAllQuestionsByUuid() {
        String uuid = "246546654546654";
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findAllByUuid(uuid)).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllQuestionsByUuid(uuid);

        assertEquals(expectedQuizzes, result);

        verify(quizRepository).findAllByUuid(uuid);
    }

    @Test
    void testDeleteQuiz() {
        Long quizId = 1L;
        Quiz quiz = new Quiz();

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        quizService.deleteQuiz(quizId);

        verify(quizRepository).findById(quizId);
        verify(quizRepository).delete(quiz);
    }

    @Test
    void testDeleteQuizExeption() {
        Long quizId = 1L;

        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> quizService.deleteQuiz(quizId));

        verify(quizRepository).findById(quizId);
        verify(quizRepository, never()).delete(any());
    }

}
