package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.GraphDTO;
import com.quizzapp.quizzmaker.dto.QuizDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.services.GraphService;
import com.quizzapp.quizzmaker.services.QuizService;
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
@RequestMapping("/api/quiz")
public class QuizController {

    @Autowired
    private final QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private final GraphService graphService;




    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Quiz> getAllUserQuizzes(@PathVariable @Valid Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> authenticatedUser = userService.getUser(id);

        if (userService.getUser(authentication.getName()).getId() == id && authenticatedUser.isPresent()) {
            return quizService.getAllUserQuizzes((long) id);
        } else {
            if (!authenticatedUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            } else {
                throw new AccessDeniedException("You are not authorized to see this quizzes.");
            }
        }
    }

    @GetMapping("/")
    public List<Quiz> getAllPublicQuizzes() {
        return quizService.getAllPublicQuizzes();
    }

    @GetMapping("/{id}/questions")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public List<Quiz> getAllQuestions(@PathVariable @Valid Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Quiz> quiz = quizService.getQuiz(id);

        if (quiz.isPresent()) {
            Quiz quizObj = quiz.get();

            if (userService.getUser(authentication.getName()).getId() == quizObj.getUser().getId()) {
                return quizService.getAllQuestions(id);
            } else {
                throw new AccessDeniedException("You are not authorized to get questions for this quiz.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

    }

    @GetMapping("/{uuid}/questions/uuid")
    public List<Quiz> getAllQuestionsByUuid(@PathVariable @Valid String uuid) {
        return quizService.getAllQuestionsByUuid(uuid);
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Quiz createQuiz(@RequestBody @Valid QuizDTO quizDTO) {
        return quizService.createQuiz(quizDTO);
    }

    @PutMapping(path = "/edit")
    public Quiz updateQuiz(@RequestBody @Valid QuizDTO quizDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Quiz> optQuiz = quizService.getQuiz((long) quizDTO.getId());

        if (optQuiz.isPresent()) {
            Quiz quiz = optQuiz.get();
            if (userService.getUser(authentication.getName()).getId() == quiz.getUser().getId()) {
                return quizService.updateQuiz(quizDTO);
            } else {
                throw new AccessDeniedException("You are not authorized to edit this quiz.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }

    }


    @DeleteMapping(path = "/delete/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public void deleteQuiz(@PathVariable @Valid Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Quiz> optQuiz = quizService.getQuiz(id);

        if (optQuiz.isPresent()) {
            Quiz quiz = optQuiz.get();
            if (userService.getUser(authentication.getName()).getId() == quiz.getUser().getId()) {
                quizService.deleteQuiz(id);
            } else {
                throw new AccessDeniedException("You are not authorized to delete this quiz.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
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
