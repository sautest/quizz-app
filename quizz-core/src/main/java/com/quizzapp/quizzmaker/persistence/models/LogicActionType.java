package com.quizzapp.quizzmaker.persistence.models;

import lombok.Getter;

@Getter
public enum LogicActionType {
    SKIP_TO_QUESTION("SKIP_TO_QUESTION"),
    END_QUESTIONING("END_QUESTIONING");

    private final String type;

    LogicActionType(String type) {
        this.type = type;
    }
}
