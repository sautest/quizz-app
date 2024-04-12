package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.AnswerDTO;
import com.quizzapp.quizzmaker.persistence.entities.Answer;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.QuestionOption;
import com.quizzapp.quizzmaker.persistence.repositories.AnswerRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionOptionRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionRepository;
import com.quizzapp.quizzmaker.services.impl.AnswerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnswerImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionOptionRepository questionOptionRepository;

    @InjectMocks
    private AnswerImpl answerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAnswer_Successful() {
        Long questionId = 1L;
        List<Long> selectedOptionIds = List.of(1L, 2L);
        String participantName = "John Doe";
        int participantAge = 30;

        AnswerDTO answerDTO = new AnswerDTO(questionId, selectedOptionIds, participantName, participantAge);
        List<AnswerDTO> answerDTOList = List.of(answerDTO);

        Question question = new Question();
        question.setId(questionId.intValue());

        List<QuestionOption> selectedOptions = selectedOptionIds.stream()
                .map(id -> {
                    QuestionOption option = new QuestionOption();
                    option.setId(id.intValue());
                    return option;
                })
                .collect(Collectors.toList());

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
        when(questionOptionRepository.findAllById(selectedOptionIds)).thenReturn(selectedOptions);
        when(answerRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0, List.class)); // Return the same list that's passed in

        List<Answer> answers = answerService.createAnswer(answerDTOList);

        assertNotNull(answers, "Answers should not be null");
        assertEquals(1, answers.size(), "There should be 1 answer");
        Answer answer = answers.get(0);
        assertEquals(question, answer.getQuestion(), "The question in the answer should match the provided question");
        assertEquals(selectedOptions, answer.getSelectedOptions(), "The selected options in the answer should match the provided selected options");
        assertEquals(participantName, answer.getParticipantName(), "The participant name should match");
        assertEquals(participantAge, answer.getParticipantAge(), "The participant age should match");

        verify(questionRepository).findById(questionId);
        verify(questionOptionRepository).findAllById(selectedOptionIds);
        verify(answerRepository).saveAll(anyList());
    }



    @Test
    void createAnswer_QuestionNotFound_ThrowsException() {
        Long nonExistentQuestionId = 99L;
        AnswerDTO answerDTO = new AnswerDTO(nonExistentQuestionId, List.of(1L, 2L), "John Doe", 30);
        List<AnswerDTO> answerDTOList = List.of(answerDTO);

        when(questionRepository.findById(nonExistentQuestionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> answerService.createAnswer(answerDTOList), "Expected an IllegalArgumentException to be thrown when the question is not found");

        verify(questionRepository).findById(nonExistentQuestionId);
        verifyNoInteractions(questionOptionRepository);
        verifyNoInteractions(answerRepository);
    }

    @Test
    void getAnswers_Successful() {
        Long questionId = 1L;
        List<Answer> expectedAnswers = Arrays.asList(new Answer(), new Answer());

        when(answerRepository.findAllByQuestionId(questionId)).thenReturn(expectedAnswers);

        List<Answer> answers = answerService.getAnswers(questionId);

        assertNotNull(answers, "Answers should not be null");
        assertEquals(expectedAnswers.size(), answers.size(), "The number of returned answers should match the expected");

        verify(answerRepository).findAllByQuestionId(questionId);
    }


}
