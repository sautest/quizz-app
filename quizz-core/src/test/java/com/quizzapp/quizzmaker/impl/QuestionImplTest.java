package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.models.QuestionType;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.SurveyRepository;
import com.quizzapp.quizzmaker.services.impl.QuestionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private QuestionImpl questionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUserQuestions_Successful() {
        Long ownerId = 1L;
        List<Question> expectedQuestions = List.of(new Question(), new Question());
        when(questionRepository.findAllByOwnerId(ownerId)).thenReturn(expectedQuestions);

        List<Question> questions = questionService.getAllUserQuestions(ownerId);

        assertNotNull(questions, "The list of questions should not be null");
        assertEquals(expectedQuestions.size(), questions.size(), "The list should contain the expected number of questions");

        verify(questionRepository).findAllByOwnerId(ownerId);
    }

    @Test
    void getQuestion_Found() {
        Long questionId = 1L;
        Question expectedQuestion = new Question();
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(expectedQuestion));

        Optional<Question> question = questionService.getQuestion(questionId);

        assertTrue(question.isPresent(), "Question should be found");
        assertEquals(expectedQuestion, question.get(), "The found question should match the expected question");

        verify(questionRepository).findById(questionId);
    }

    @Test
    void getQuestion_NotFound() {
        Long nonExistentQuestionId = 99L;
        when(questionRepository.findById(nonExistentQuestionId)).thenReturn(Optional.empty());

        Optional<Question> question = questionService.getQuestion(nonExistentQuestionId);

        assertFalse(question.isPresent(), "Question should not be found");

        verify(questionRepository).findById(nonExistentQuestionId);
    }

    @Test
    void createQuestion_AssociateWithQuiz_Successful() {
        int quizId = 1;
        String questionText = "What is the capital of France?";
        QuestionType questionType = QuestionType.SINGLE_CHOICE;
        int score = 10;
        int ownerId = 1;

        Question questionForQuiz = new Question();
        questionForQuiz.setText(questionText);
        questionForQuiz.setType(questionType);
        questionForQuiz.setScore(score);
        questionForQuiz.setOwnerId(ownerId);

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setProjectId((long) quizId);
        questionDTO.setType("QUIZ");
        questionDTO.setQuestion(questionForQuiz);

        Question savedQuestion = new Question();
        savedQuestion.setId(1);

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setQuestions(new ArrayList<>());

        when(questionRepository.save(any(Question.class))).thenReturn(savedQuestion);
        when(questionRepository.findById((long) savedQuestion.getId())).thenReturn(Optional.of(savedQuestion));
        when(quizRepository.findById((long) quizId)).thenReturn(Optional.of(quiz));

        Question result = questionService.createQuestion(questionDTO);

        assertNotNull(result, "The created question should not be null");
        assertEquals(savedQuestion.getId(), result.getId(), "The IDs should match");

        verify(questionRepository).save(any(Question.class));
        verify(quizRepository).findById((long) quizId);
        verify(quizRepository).save(quiz);
    }

    @Test
    void createQuestion_AssociateWithSurvey_Successful() {
        int surveyId = 1;
        String questionText = "What is your favorite color?";
        QuestionType questionType = QuestionType.MULTI_CHOICE;
        int score = 0;
        int ownerId = 1;

        Question questionForSurvey = new Question();
        questionForSurvey.setText(questionText);
        questionForSurvey.setType(questionType);
        questionForSurvey.setScore(score);
        questionForSurvey.setOwnerId(ownerId);

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setProjectId((long) surveyId);
        questionDTO.setType("SURVEY");
        questionDTO.setQuestion(questionForSurvey);

        Question savedQuestion = new Question();
        savedQuestion.setId(1);

        Survey survey = new Survey();
        survey.setId(surveyId);
        survey.setQuestions(new ArrayList<>());

        when(questionRepository.save(any(Question.class))).thenReturn(savedQuestion);
        when(questionRepository.findById((long) savedQuestion.getId())).thenReturn(Optional.of(savedQuestion));
        when(surveyRepository.findById((long) surveyId)).thenReturn(Optional.of(survey));


        Question result = questionService.createQuestion(questionDTO);

        assertNotNull(result, "The created question should not be null");
        assertEquals(savedQuestion.getId(), result.getId(), "The IDs should match");

        verify(questionRepository).save(any(Question.class));
        verify(surveyRepository).findById((long) surveyId);
        verify(surveyRepository).save(survey);
    }


    @Test
    void updateQuestion_Successful() {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = new Question();
        question.setId(1);
        question.setText("Original Text");
        question.setType(QuestionType.SINGLE_CHOICE);
        question.setScore(5);

        Question updatedQuestion = new Question();
        updatedQuestion.setId(question.getId());
        updatedQuestion.setText("Updated Text");
        updatedQuestion.setType(QuestionType.MULTI_CHOICE);
        updatedQuestion.setScore(10);

        questionDTO.setQuestion(updatedQuestion);

        when(questionRepository.findById((long) question.getId())).thenReturn(Optional.of(question));
        when(questionRepository.save(any(Question.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Question result = questionService.updateQuestion(questionDTO);

        assertEquals(updatedQuestion.getText(), result.getText());
        assertEquals(updatedQuestion.getType(), result.getType());
        assertEquals(updatedQuestion.getScore(), result.getScore());

        verify(questionRepository).findById((long) question.getId());
        verify(questionRepository).save(any(Question.class));
    }

    @Test
    void deleteQuestion_Successful() {
        int questionId = 1;
        Question question = new Question();
        question.setId(questionId);

        Quiz quiz = new Quiz();
        quiz.setId(1);
        quiz.setQuestions(new ArrayList<>(List.of(question)));

        Survey survey = new Survey();
        survey.setId(1);
        survey.setQuestions(new ArrayList<>(List.of(question)));

        when(questionRepository.findById((long) questionId)).thenReturn(Optional.of(question));

        questionService.deleteQuestion((long) questionId);

        verify(questionRepository, times(1)).findById((long) questionId);
        verify(questionRepository, times(1)).delete(question);
    }

    @Test
    void deleteQuestion_QuestionNotFound() {
        long questionId = 1L;
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> questionService.deleteQuestion(questionId));
        verify(questionRepository, times(1)).findById(questionId);
        verify(questionRepository, never()).delete(any());
        verify(quizRepository, never()).save(any());
        verify(surveyRepository, never()).save(any());
    }






}
