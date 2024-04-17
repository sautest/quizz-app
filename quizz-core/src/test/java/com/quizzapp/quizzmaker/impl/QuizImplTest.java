package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.*;
import com.quizzapp.quizzmaker.persistence.models.ProjectStatus;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.QuizService;
import com.quizzapp.quizzmaker.services.impl.QuizImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

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
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void createQuiz_Successful() {

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(0);
        quizDTO.setUserId(1);
        quizDTO.setTitle("Sample Quiz");
        quizDTO.setDateCreated("2024-03-20");
        quizDTO.setSettings(new Settings());
        quizDTO.setTheme(new Theme());

        User user = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(quizDTO.getUserId())).thenReturn(Optional.of(user));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> {
            Quiz savedQuiz = invocation.getArgument(0);
            savedQuiz.setId(1);
            return savedQuiz;
        });

        Quiz result = quizService.createQuiz(quizDTO);


        assertNotNull(result, "The created quiz should not be null");
        assertNotNull(result.getId(), "The ID of the created quiz should not be null");
        assertEquals(quizDTO.getTitle(), result.getTitle(), "The titles should match");
        assertNotNull(result.getDateCreated(), "The date created of the created quiz should not be null");
        assertEquals(user, result.getUser(), "The user of the created quiz should match the provided user");
        assertEquals(quizDTO.getSettings(), result.getSettings(), "The settings of the created quiz should match");
        assertEquals(quizDTO.getTheme(), result.getTheme(), "The theme of the created quiz should match");

        verify(userRepository, times(1)).findById(quizDTO.getUserId());
    }


    @Test
    void updateQuiz_Successful() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(1);
        quizDTO.setTitle("Updated Quiz Title");
        quizDTO.setQuestions(new ArrayList<>()) ;
        quizDTO.setSettings(new Settings());
        quizDTO.setTheme(new Theme());
        quizDTO.setStatus(ProjectStatus.IN_DESIGN);
        quizDTO.setResponses(10);

        Quiz existingQuiz = new Quiz();

        when(quizRepository.findById((long) quizDTO.getId())).thenReturn(Optional.of(existingQuiz));
        when(quizRepository.save(any(Quiz.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Quiz updatedQuiz = quizService.updateQuiz(quizDTO);

        assertNotNull(updatedQuiz, "The updated quiz should not be null");
        assertEquals(quizDTO.getTitle(), updatedQuiz.getTitle(), "The titles should match");
        assertEquals(quizDTO.getQuestions(), updatedQuiz.getQuestions(), "The questions should match");
        assertEquals(quizDTO.getSettings(), updatedQuiz.getSettings(), "The settings should match");
        assertEquals(quizDTO.getTheme(), updatedQuiz.getTheme(), "The theme should match");
        assertEquals(quizDTO.getStatus(), updatedQuiz.getStatus(), "The status should match");
        assertEquals(quizDTO.getResponses(), updatedQuiz.getResponses(), "The responses should match");

        verify(quizRepository, times(1)).findById((long) quizDTO.getId());
        verify(quizRepository, times(1)).save(any(Quiz.class));
    }


    @Test
    void getAllUserQuizzes_Successful() {
        Long userId = 1L;
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findByUserId(userId)).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllUserQuizzes(userId);

        assertEquals(expectedQuizzes, result, "Returned quizzes should match the expected quizzes");

        verify(quizRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getAllPublicQuizzes_Successful() {
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findAll()).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllPublicQuizzes();

        assertEquals(expectedQuizzes, result, "Returned public quizzes should match the expected quizzes");

        verify(quizRepository, times(1)).findAll();
    }

    @Test
    void getQuiz_Successful() {
        Long quizId = 1L;
        Quiz expectedQuiz = new Quiz();

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(expectedQuiz));

        Optional<Quiz> result = quizService.getQuiz(quizId);

        assertEquals(Optional.of(expectedQuiz), result, "Returned quiz should match the expected quiz");

        verify(quizRepository, times(1)).findById(quizId);
    }

    @Test
    void getAllQuestions_Successful() {
        Long quizId = 1L;
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findAllById(quizId)).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllQuestions(quizId);

        assertEquals(expectedQuizzes, result, "Returned quizzes should match the expected quizzes");

        verify(quizRepository, times(1)).findAllById(quizId);
    }

    @Test
    void getAllQuestionsByUuid_Successful() {
        String uuid = "example_uuid";
        List<Quiz> expectedQuizzes = new ArrayList<>();

        when(quizRepository.findAllByUuid(uuid)).thenReturn(expectedQuizzes);

        List<Quiz> result = quizService.getAllQuestionsByUuid(uuid);

        assertEquals(expectedQuizzes, result, "Returned quizzes should match the expected quizzes");

        verify(quizRepository, times(1)).findAllByUuid(uuid);
    }

    @Test
    void deleteQuiz_Successful() {
        Long quizId = 1L;
        Quiz quiz = new Quiz();

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        quizService.deleteQuiz(quizId);

        verify(quizRepository, times(1)).findById(quizId);
        verify(quizRepository, times(1)).delete(quiz);
    }

    @Test
    void deleteQuiz_QuizNotFound() {
        Long quizId = 1L;

        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> quizService.deleteQuiz(quizId));

        verify(quizRepository, times(1)).findById(quizId);
        verify(quizRepository, never()).delete(any());
    }

}
