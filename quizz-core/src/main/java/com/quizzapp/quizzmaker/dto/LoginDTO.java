package com.quizzapp.quizzmaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {

    private Integer id;
    private String token;

    private ResponseDTO response;

}
