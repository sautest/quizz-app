package com.quizzapp.quizzmaker.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "user")
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id",length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="email")
    private String email;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="blocked")
    private boolean blocked;
    @Column(name="dateJoined")
    private String dateJoined;
    @Column(name="roles")
    private String roles;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private List<Quiz> quiz;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private List<Survey> survey;


    public User(int id, String email, String username, String password, boolean blocked,
                String dateJoined, String roles, List<Quiz> quiz, List<Survey> survey) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.blocked = blocked;
        this.dateJoined = dateJoined;
        this.roles = roles;
        this.quiz = quiz;
        this.survey = survey;
    }

}
