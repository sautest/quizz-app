package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.services.QuestionService;
import com.quizzapp.quizzmaker.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Question createQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        return questionService.createQuestion(questionDTO);
    }

    @PostMapping(path = "/edit")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Question updateQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        return questionService.updateQuestion(questionDTO);
    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public void updateQuestion(@PathVariable @Valid Long id) {
         questionService.deleteQuestion(id);
    }
}
