package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionOptionRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.SurveyRepository;
import com.quizzapp.quizzmaker.services.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionImpl implements QuestionService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @Override
    public Question createQuestion(QuestionDTO questionDTO) {
        int questionId = questionRepository.save(questionDTO.getQuestion()).getId();
        Question question = questionRepository.findById((long) questionId).orElseThrow(() -> new RuntimeException("Question not found"));

        if ("QUIZ".equals(questionDTO.getType())) {
            Quiz quiz = quizRepository.findById(questionDTO.getProjectId()).orElseThrow(() -> new RuntimeException("Quiz not found"));

            List<Question> questionList = quiz.getQuestions();
            questionList.add((question));

            quiz.setQuestions(questionList);
            quizRepository.save(quiz);
        } else {
            Survey survey = surveyRepository.findById(questionDTO.getProjectId()).orElseThrow(() -> new RuntimeException("Survey not found"));

            List<Question> surveyList = survey.getQuestions();
            surveyList.add((question));

            survey.setQuestions(surveyList);
            surveyRepository.save(survey);

        }

        return question;
    }

    @Override
    public Question updateQuestion(QuestionDTO questionDTO) {

        Question question = questionRepository.findById((long) questionDTO.getQuestion().getId()).orElseThrow(() -> new RuntimeException("Question not found"));

        question.setType(questionDTO.getQuestion().getType());
        question.setText(questionDTO.getQuestion().getText());
        
        question.setOptions(questionDTO.getQuestion().getOptions());

        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));

        question.getQuizzes().forEach(quiz -> quiz.getQuestions().remove(question));
        question.getSurveys().forEach(survey -> survey.getQuestions().remove(question));

        questionRepository.delete(question);

    }


}
