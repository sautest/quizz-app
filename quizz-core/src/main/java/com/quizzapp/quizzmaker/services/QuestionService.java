package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    List<Question> getAllUserQuestions(Long id);

    Optional<Question> getQuestion(Long id);
    Question createQuestion(QuestionDTO questionDTO);

    Question updateQuestion(QuestionDTO questionDTO);

    void deleteQuestion(Long id);

}
