package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.LoginDTO;
import com.quizzapp.quizzmaker.dto.ResponseDTO;
import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.AuthRequest;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.security.JWTService;
import com.quizzapp.quizzmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;


    @PostMapping(path = "/create")
    public User createUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.createUser(userDTO);
    }


    @PostMapping("/login")
    public LoginDTO addUser(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return new LoginDTO(userService.getUser(authRequest.getUsername()).getId(),
                       jwtService.generateToken(authRequest.getUsername()),
                       new ResponseDTO("Login success", "SUCCESS"));
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }


    @PreAuthorize("hasAuthority('USER_ROLES')")
    @GetMapping("/getUsers/{id}")
    public User getAllUsers(@PathVariable Integer id) {
        return userService.getUser(id);
    }


}
