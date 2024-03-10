package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.QuizService;
import com.quizzapp.quizzmaker.services.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {
    @Autowired
    private final SurveyService surveyService;

    @Autowired
    private final GraphService graphService;

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

    @GetMapping("/{uuid}/questions/uuid")
    public List<Survey> getAllQuestionsByUuid(@PathVariable @Valid String uuid){
        return surveyService.getAllQuestionsByUuid(uuid);
    }

    @GetMapping("/")
    public List<Survey> getAllSurveys(){
        return surveyService.getAllSurveys();
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Survey createSurvey(@RequestBody @Valid SurveyDTO surveyDTO) {
        return surveyService.createSurvey(surveyDTO);
    }

    @PostMapping(path = "/generate")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public ResponseEntity<Map<String, String>> generateQuizGraph(@RequestBody @Valid GraphDTO graphDTO) {

        Map<String, String> response = new HashMap<>();
        response.put("graph", graphService.generate(graphDTO));
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/edit")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Survey updateSurvey(@RequestBody @Valid SurveyDTO surveyDTO) {
        return surveyService.updateSurvey(surveyDTO);
    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public void deleteSurvey(@PathVariable @Valid Long id) {
        surveyService.deleteSurvey(id);
    }

   
}


