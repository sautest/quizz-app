package com.quizzapp.quizzmaker.dto;

import com.quizzapp.quizzmaker.persistence.entities.Question;
import com.quizzapp.quizzmaker.persistence.entities.Settings;
import com.quizzapp.quizzmaker.persistence.entities.Theme;
import com.quizzapp.quizzmaker.persistence.models.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO {
    private int id;
    private int userId;
    private String title;
    private int responses;
    private String dateCreated;

    private ProjectStatus status;
    private List<Question> questions;
    private Settings settings;
    private Theme theme;
}
