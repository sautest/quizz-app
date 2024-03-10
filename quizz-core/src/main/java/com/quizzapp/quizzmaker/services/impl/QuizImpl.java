package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.models.ProjectStatus;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private UserRepository userRepository;




    @Override
    public Quiz createQuiz(QuizDTO quizDTO) {
        User user = userRepository.findById(quizDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(UUID.randomUUID().toString());
        Quiz quiz = new Quiz(quizDTO.getId(), UUID.randomUUID().toString().substring(0, 36),quizDTO.getTitle(),0,quizDTO.getDateCreated(),
                ProjectStatus.IN_DESIGN,user,new ArrayList<>(),quizDTO.getSettings(),quizDTO.getTheme());
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
        quiz.setTheme(quizDTO.getTheme());
        quiz.setStatus(quizDTO.getStatus());
        quiz.setResponses(quizDTO.getResponses());
        return quizRepository.save(quiz);
    }


    @Override
    public List<Quiz> getAllUserQuizzes(Long id) {
        return quizRepository.findByUserId(id);
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Override
    public List<Quiz> getAllQuestions(Long id) {
        return quizRepository.findAllById(id);
    }

    @Override
    public List<Quiz> getAllQuestionsByUuid(String uuid) {
        return quizRepository.findAllByUuid(uuid);
    }

    @Override
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found with id: " + id));

        quizRepository.delete(quiz);
    }
}
