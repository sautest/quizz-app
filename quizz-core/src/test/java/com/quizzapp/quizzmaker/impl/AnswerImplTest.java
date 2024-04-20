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
    void testCreateAnswer() {
        Long questionId = 1L;
        List<Long> selectedOptionIds = List.of(1L, 2L);
        String participantName = "bla bla";
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
        when(answerRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0, List.class));

        List<Answer> answers = answerService.createAnswer(answerDTOList);

        assertNotNull(answers);
        assertEquals(1, answers.size());
        Answer answer = answers.get(0);
        assertEquals(question, answer.getQuestion());
        assertEquals(selectedOptions, answer.getSelectedOptions());
        assertEquals(participantName, answer.getParticipantName());
        assertEquals(participantAge, answer.getParticipantAge());

        verify(questionRepository).findById(questionId);
        verify(questionOptionRepository).findAllById(selectedOptionIds);
        verify(answerRepository).saveAll(anyList());
    }



    @Test
    void testCreateAnswerException() {
        Long questionId = 99L;
        AnswerDTO answerDTO = new AnswerDTO(questionId, List.of(1L, 2L), "bla bla", 30);
        List<AnswerDTO> answerDTOList = List.of(answerDTO);

        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> answerService.createAnswer(answerDTOList));

        verify(questionRepository).findById(questionId);
        verifyNoInteractions(questionOptionRepository);
        verifyNoInteractions(answerRepository);
    }

    @Test
    void testGetAnswers() {
        Long questionId = 1L;
        List<Answer> expectedAnswers = Arrays.asList(new Answer(), new Answer());

        when(answerRepository.findAllByQuestionId(questionId)).thenReturn(expectedAnswers);

        List<Answer> answers = answerService.getAnswers(questionId);

        assertNotNull(answers);
        assertEquals(expectedAnswers.size(), answers.size());

        verify(answerRepository).findAllByQuestionId(questionId);
    }


}
