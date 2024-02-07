package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.services.QuizService;
import com.quizzapp.quizzmaker.services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {
    @Autowired
    private final SurveyService surveyService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public List<Survey> getAllUserSurveys(@PathVariable @Valid Long id){
        return surveyService.getAllUserSurveys(id);
    }

    @GetMapping("/{id}/questions")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public List<Survey> getAllQuestions(@PathVariable @Valid Long id){
        return surveyService.getAllQuestions(id);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Survey createSurvey(@RequestBody @Valid SurveyDTO surveyDTO) {
        return surveyService.createSurvey(surveyDTO);
    }

    @PutMapping(path = "/edit")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Survey updateSurvey(@RequestBody @Valid SurveyDTO surveyDTO) {
        return surveyService.updateSurvey(surveyDTO);
    }

   
}


