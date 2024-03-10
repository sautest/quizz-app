package com.quizzapp.quizzmaker.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quizzapp.quizzmaker.persistence.models.LogicActionType;
import com.quizzapp.quizzmaker.persistence.models.LogicConditionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionLogic {
    @Id
    @Column(name = "id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private Question question;


    private LogicConditionType conditionType;

    private int conditionOptionId;

    private LogicActionType actionType;

    private int actionOptionId;


}
