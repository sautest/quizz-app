package com.quizzapp.quizzmaker.dto;

import com.quizzapp.quizzmaker.persistence.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Long projectId;
    private String type;
    private Question question;


}

