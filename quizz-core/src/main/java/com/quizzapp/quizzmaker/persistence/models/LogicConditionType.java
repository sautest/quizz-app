package com.quizzapp.quizzmaker.persistence.models;

import lombok.Getter;

@Getter
public enum LogicConditionType {
    WHEN_ANSWER_IS("WHEN_ANSWER_IS"),
    ALWAYS("ALWAYS");

    private final String type;

    LogicConditionType(String type) {
        this.type = type;
    }
}
