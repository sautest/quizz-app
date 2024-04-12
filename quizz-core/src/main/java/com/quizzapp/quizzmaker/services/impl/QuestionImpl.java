package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuizRepository;
import com.quizzapp.quizzmaker.persistence.repositories.SurveyRepository;
import com.quizzapp.quizzmaker.services.QuestionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionImpl implements QuestionService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @Override
    public List<Question> getAllUserQuestions(Long id) {
        return  questionRepository.findAllByOwnerId(id);
    }

    @Override
    public Optional<Question> getQuestion(Long id) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        return optionalQuestion;
    }


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
        System.out.println(questionDTO);

        Question question = questionRepository.findById((long) questionDTO.getQuestion().getId()).orElseThrow(() -> new RuntimeException("Question not found"));

        question.setType(questionDTO.getQuestion().getType());
        question.setText(questionDTO.getQuestion().getText());

        question.setScore(questionDTO.getQuestion().getScore());
        
        question.setOptions(questionDTO.getQuestion().getOptions());
        question.setLogic(questionDTO.getQuestion().getLogic());

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
