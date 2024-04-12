package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.dto.SurveyDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.QuizService;
import com.quizzapp.quizzmaker.services.SurveyService;
import com.quizzapp.quizzmaker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {
    @Autowired
    private final SurveyService surveyService;

    @Autowired
    private UserService userService;

    @Autowired
    private final GraphService graphService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Survey> getAllUserSurveys(@PathVariable @Valid Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> authenticatedUser = userService.getUser(id);

        if (userService.getUser(authentication.getName()).getId() == id && authenticatedUser.isPresent()) {
            return surveyService.getAllUserSurveys((long) id);
        } else {
            if (!authenticatedUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            } else {
                throw new AccessDeniedException("You are not authorized to see this quizzes.");
            }
        }
    }

    @GetMapping("/")
    public List<Survey> getAllPublicSurveys() {
        return surveyService.getAllPublicSurveys();
    }

    @GetMapping("/{id}/questions")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Survey> getAllQuestions(@PathVariable @Valid Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Survey> survey = surveyService.getSurvey(id);

        if (survey.isPresent()) {
            Survey surveyObj = survey.get();

            if (userService.getUser(authentication.getName()).getId() == surveyObj.getUser().getId()) {
                return surveyService.getAllQuestions(id);
            } else {
                throw new AccessDeniedException("You are not authorized to get questions for this survey.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }

    }

    @GetMapping("/{uuid}/questions/uuid")
    public List<Survey> getAllQuestionsByUuid(@PathVariable @Valid String uuid) {
        return surveyService.getAllQuestionsByUuid(uuid);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Survey createSurvey(@RequestBody @Valid SurveyDTO surveyDTO) {
        return surveyService.createSurvey(surveyDTO);
    }

    @PutMapping(path = "/edit")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Survey updateSurvey(@RequestBody @Valid SurveyDTO surveyDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Survey> optSurvey = surveyService.getSurvey((long) surveyDTO.getId());

        if (optSurvey.isPresent()) {
            Survey survey = optSurvey.get();
            if (userService.getUser(authentication.getName()).getId() == survey.getUser().getId()) {
                return surveyService.updateSurvey(surveyDTO);
            } else {
                throw new AccessDeniedException("You are not authorized to edit this survey.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }


    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public void deleteSurvey(@PathVariable @Valid Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Survey> optSurvey = surveyService.getSurvey(id);

        if (optSurvey.isPresent()) {
            Survey survey = optSurvey.get();
            if (userService.getUser(authentication.getName()).getId() == survey.getUser().getId()) {
                surveyService.deleteSurvey(id);
            } else {
                throw new AccessDeniedException("You are not authorized to delete this quiz.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Survey not found");
        }
    }


    @PostMapping(path = "/generate")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public ResponseEntity<Map<String, String>> generateQuizGraph(@RequestBody @Valid GraphDTO graphDTO) {

        Map<String, String> response = new HashMap<>();
        response.put("graph", graphService.generate(graphDTO));
        return ResponseEntity.ok(response);
    }
}


