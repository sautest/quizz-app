package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.services.HealthService;
import com.quizzapp.quizzmaker.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private final QuizService quizService;

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public List<Quiz> getAllUserQuizzes(@PathVariable @Valid Long id){
        return quizService.getAllUserQuizzes(id);
    }


    @GetMapping("/{id}/questions")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public List<Quiz> getAllQuestions(@PathVariable @Valid Long id){
        return quizService.getAllQuestions(id);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Quiz createQuiz(@RequestBody @Valid QuizDTO quizDTO) {
        return quizService.createQuiz(quizDTO);
    }
    @PutMapping(path = "/edit")
    @PreAuthorize("hasAuthority('USER_ROLES')")
    public Quiz updateQuiz(@RequestBody @Valid QuizDTO quizDTO) {
        return quizService.updateQuiz(quizDTO);
    }



}
