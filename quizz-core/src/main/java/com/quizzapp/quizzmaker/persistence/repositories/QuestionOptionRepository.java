package com.quizzapp.quizzmaker.persistence.repositories;

import com.quizzapp.quizzmaker.persistence.entities.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository   extends JpaRepository<QuestionOption,Long> {

}
