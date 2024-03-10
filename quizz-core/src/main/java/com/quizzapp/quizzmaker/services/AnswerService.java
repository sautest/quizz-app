package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.dto.AnswerDTO;
import com.quizzapp.quizzmaker.persistence.entities.Answer;
import com.quizzapp.quizzmaker.persistence.entities.User;

import java.util.List;

public interface AnswerService {
    List<Answer> createAnswer(List<AnswerDTO> answerDTO);

    List<Answer> getAnswers(Long id);
}
