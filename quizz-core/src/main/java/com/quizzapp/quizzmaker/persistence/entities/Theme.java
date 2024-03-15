package com.quizzapp.quizzmaker.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Theme {
    @Id
    @Column(name = "id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String bgColor;
    @Column(length = 10000000)
    private String bgImage;

    private String questionColor;

    private String optionBgColor;
    private String optionTextColor;

    private String buttonBgColor;
    private String buttonTextColor;
}
