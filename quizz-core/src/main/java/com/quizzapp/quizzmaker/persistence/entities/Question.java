package com.quizzapp.quizzmaker.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quizzapp.quizzmaker.persistence.models.QuestionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @Column(name = "id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String text;
    private QuestionType type;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id",referencedColumnName = "id")
    private List<QuestionOption> options;

    @ManyToMany(mappedBy = "questions")
    @JsonIgnoreProperties("questions")
    private List<Quiz> quizzes = new ArrayList<>();

    @ManyToMany(mappedBy = "questions")
    @JsonIgnoreProperties("questions")
    private List<Survey> surveys = new ArrayList<>();

}
