package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.AnswerDTO;
import com.quizzapp.quizzmaker.persistence.entities.Answer;
import com.quizzapp.quizzmaker.services.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
public class AnswerController {

    @Autowired
    private final AnswerService answerService;

    @PostMapping(path = "/create")
    public List<Answer> createAnswer(@RequestBody @Valid List<AnswerDTO> answerDTO) {
        return answerService.createAnswer(answerDTO);
    }

    @GetMapping("/getAnswers/{id}")
    public List<Answer> getAnswers(@PathVariable Long id) {
        return answerService.getAnswers(id);
    }

}

