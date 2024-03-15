package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.QuestionDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private final QuestionService questionService;


    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Question> getQuestions(@PathVariable @Valid Long id) {
       return questionService.getAllUserQuestions(id);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Question createQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        return questionService.createQuestion(questionDTO);
    }

    @PostMapping(path = "/edit")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Question updateQuestion(@RequestBody @Valid QuestionDTO questionDTO) {
        return questionService.updateQuestion(questionDTO);
    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public void deleteQuestion(@PathVariable @Valid Long id) {
         questionService.deleteQuestion(id);
    }
}
