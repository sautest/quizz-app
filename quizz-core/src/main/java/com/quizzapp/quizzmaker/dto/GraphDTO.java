package com.quizzapp.quizzmaker.dto;

import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphDTO {

    private Quiz quiz;
    private Survey survey;

    private Boolean isVerticalAlignment;

}
