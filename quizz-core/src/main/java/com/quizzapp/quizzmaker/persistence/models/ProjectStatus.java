package com.quizzapp.quizzmaker.persistence.models;

import lombok.Getter;

@Getter
public enum ProjectStatus {

    IN_DESIGN("IN_DESIGN"),
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String status;

    ProjectStatus(String status) {
        this.status = status;
    }
}
