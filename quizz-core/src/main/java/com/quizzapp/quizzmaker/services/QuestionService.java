package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;

import java.util.List;

public interface QuestionService {

    Question createQuestion(QuestionDTO questionDTO);

    Question updateQuestion(QuestionDTO questionDTO);

    void deleteQuestion(Long id);

}
