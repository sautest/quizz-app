package com.quizzapp.quizzmaker.services;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;

import java.util.List;


public interface QuizService {

    List<Quiz> getAllUserQuizzes(Long id);

    List<Quiz> getAllQuestions(Long id);

    Quiz createQuiz(QuizDTO quizDTO);

    Quiz updateQuiz(QuizDTO quizDTO);
}
