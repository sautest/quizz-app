package com.quizzapp.quizzmaker.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Settings {
    @Id
    @Column(name = "id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private boolean enableTimeLimit;
    @Column(nullable = true)
    private int min ;

    @Column(nullable = true)
    private int hours ;

    private int questionsPerPage;

    private boolean enableAnswerNotInOrder;
    private boolean enableAskForBasicUserInfo;
    private boolean enableProgressBar;
    private boolean enableRandomizeQuestions;
    private boolean enablePublic;
    private boolean enableShowAnswersAtTheEnd;




}
