package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.QuizService;
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
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private final QuizService quizService;

    @Autowired
    private final GraphService graphService;

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Quiz> getAllUserQuizzes(@PathVariable @Valid Long id){
        return quizService.getAllUserQuizzes(id);
    }


    @GetMapping("/")
    public List<Quiz> getAllQuizzes(){
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{id}/questions")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Quiz> getAllQuestions(@PathVariable @Valid Long id){
        return quizService.getAllQuestions(id);
    }

    @GetMapping("/{uuid}/questions/uuid")
    public List<Quiz> getAllQuestionsByUuid(@PathVariable @Valid String uuid){
        return quizService.getAllQuestionsByUuid(uuid);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Quiz createQuiz(@RequestBody @Valid QuizDTO quizDTO) {
        return quizService.createQuiz(quizDTO);
    }

    @PostMapping(path = "/generate")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public ResponseEntity<Map<String, String>> generateQuizGraph(@RequestBody @Valid GraphDTO graphDTO) {

        Map<String, String> response = new HashMap<>();
        response.put("graph", graphService.generate(graphDTO));
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/edit")
    public Quiz updateQuiz(@RequestBody @Valid QuizDTO quizDTO) {
        return quizService.updateQuiz(quizDTO);
    }

    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public void deleteQuiz(@PathVariable @Valid Long id) {
        quizService.deleteQuiz(id);
    }

}
