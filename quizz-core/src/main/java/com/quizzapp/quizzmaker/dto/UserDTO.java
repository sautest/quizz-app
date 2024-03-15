package com.quizzapp.quizzmaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    private int id;
    private String email;
    private String username;
    private String password;
    private boolean blocked;
    private String dateJoined;

}
