package com.quizzapp.quizzmaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDTO {

    private Long questionId;
    private List<Long> selectedOptionIds;

    private String participantName;
    private int participantAge;
}
