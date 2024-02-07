package com.quizzapp.quizzmaker.persistence.models;

import lombok.Getter;

@Getter
public enum QuestionType {
    MULTI_CHOICE("MULTI_CHOICE"),
    SINGLE_CHOICE("SINGLE_CHOICE");

    private final String type;

    QuestionType(String type) {
        this.type = type;
    }

}
