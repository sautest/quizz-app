package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.services.QuestionService;
import com.quizzapp.quizzmaker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private UserService userService;


    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Question> getQuestions(@PathVariable @Valid Long id) {
        return questionService.getAllUserQuestions(id);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Question createQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userService.getUser(authentication.getName()).getId() == questionDTO.getQuestion().getOwnerId()) {
            return questionService.createQuestion(questionDTO);
        } else {
            throw new AccessDeniedException("You are not authorized to create question for this project.");
        }

    }

    @PostMapping(path = "/edit")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Question updateQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Question> question = questionService.getQuestion((long) questionDTO.getQuestion().getId());

        if (question.isPresent()) {
            if (userService.getUser(authentication.getName()).getId() == questionDTO.getQuestion().getOwnerId()) {
                return questionService.updateQuestion(questionDTO);
            } else {
                throw new AccessDeniedException("You are not authorized to edit question for this project.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public void deleteQuestion(@PathVariable @Valid Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Question> question = questionService.getQuestion(id);

        if (question.isPresent()) {
            Question questionObj = question.get();

            if (userService.getUser(authentication.getName()).getId() == questionObj.getOwnerId()) {
                questionService.deleteQuestion(id);
            } else {
                throw new AccessDeniedException("You are not authorized to delete question for this project.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found");
        }


    }
}
