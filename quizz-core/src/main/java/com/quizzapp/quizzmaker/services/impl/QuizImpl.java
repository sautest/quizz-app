package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;



    @Override
    public Quiz createQuiz(QuizDTO quizDTO) {
        User user = userRepository.findById(quizDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = new Quiz(quizDTO.getId(),quizDTO.getTitle(),user,new ArrayList<>(),quizDTO.getSettings());
        user.getQuiz().add(quiz);
        userRepository.save((user));
        return quiz;
    }

    @Override
    public Quiz updateQuiz(QuizDTO quizDTO) {
        Quiz quiz = quizRepository.findById((long)quizDTO.getId()).orElseThrow(() -> new RuntimeException("Quiz not found"));
        quiz.setTitle(quizDTO.getTitle());
        quiz.setQuestions(quizDTO.getQuestions());
        quiz.setSettings(quizDTO.getSettings());
        return quizRepository.save(quiz);
    }


    @Override
    public List<Quiz> getAllUserQuizzes(Long id) {
        return quizRepository.findByUserId(id);
    }

    @Override
    public List<Quiz> getAllQuestions(Long id) {
        return quizRepository.findAllById(id);
    }
}
