package com.quizzapp.quizzmaker.persistence.models;

import lombok.Getter;

@Getter
public enum LogicContitionType {
    WHEN_ANSWER_IS("WHEN_ANSWER_IS"),
    ALWAYS("ALWAYS");

    private final String type;

    LogicContitionType(String type) {
        this.type = type;
    }
}
