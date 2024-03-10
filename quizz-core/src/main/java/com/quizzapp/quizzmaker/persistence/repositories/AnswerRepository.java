package com.quizzapp.quizzmaker.persistence.repositories;

import com.quizzapp.quizzmaker.persistence.entities.Answer;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository  extends JpaRepository<Answer,Long> {

    List<Answer> findAllByQuestionId(Long questionId);
}
