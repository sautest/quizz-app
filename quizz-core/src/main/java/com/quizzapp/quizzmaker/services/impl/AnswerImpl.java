package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.AnswerDTO;
import com.quizzapp.quizzmaker.persistence.entities.Answer;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.QuestionOption;
import com.quizzapp.quizzmaker.persistence.repositories.AnswerRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionOptionRepository;
import com.quizzapp.quizzmaker.persistence.repositories.QuestionRepository;
import com.quizzapp.quizzmaker.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Override
    public List<Answer> createAnswer(List<AnswerDTO> answerDTOList) {
        List<Answer> answers = new ArrayList<>();

        for (AnswerDTO answerDTO : answerDTOList) {
            Answer answer = new Answer();
            answer.setParticipantName(answerDTO.getParticipantName());
            answer.setParticipantAge(answerDTO.getParticipantAge());

            Optional<Question> optionalQuestion = questionRepository.findById(answerDTO.getQuestionId());
            if (optionalQuestion.isPresent()) {
                Question question = optionalQuestion.get();
                answer.setQuestion(question);

                List<QuestionOption> selectedOptions = questionOptionRepository.findAllById(answerDTO.getSelectedOptionIds());
                answer.setSelectedOptions(selectedOptions);

                answers.add(answer);
            } else {
                throw new IllegalArgumentException("Question with ID " + answerDTO.getQuestionId() + " not found");
            }
        }

        return answerRepository.saveAll(answers);
    }

    @Override
    public List<Answer> getAnswers(Long id) {
        return  answerRepository.findAllByQuestionId(id);
    }
}