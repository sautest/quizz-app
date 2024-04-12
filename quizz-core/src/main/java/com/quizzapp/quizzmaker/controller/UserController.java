package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.dto.LoginDTO;
import com.quizzapp.quizzmaker.dto.ResponseDTO;
import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.AuthRequest;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.security.JWTService;
import com.quizzapp.quizzmaker.services.UserService;
import com.quizzapp.quizzmaker.services.impl.UserInfoDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<Object> addUser(@RequestBody AuthRequest authRequest) {

        try {
            if (userService.getUser(authRequest.getUsername()).isBlocked()) {
                throw new UsernameNotFoundException("This account is blocked.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This account is blocked!");

        }

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authenticate.isAuthenticated()) {
                LoginDTO loginDTO = new LoginDTO(userService.getUser(authRequest.getUsername()).getId(),
                        jwtService.generateToken(authRequest.getUsername(), userService.getUser(authRequest.getUsername()).getRoles()),
                        new ResponseDTO("Login success", "SUCCESS"));

                return ResponseEntity.ok(loginDTO);

            } else {
                throw new UsernameNotFoundException("Invalid credentials!");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials!");
        }


    }

    @GetMapping("/getUsers/{id}")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public Optional<User> getUser(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> authenticatedUser = userService.getUser(id);
        if (userService.getUser(authentication.getName()).getId() == id && authenticatedUser.isPresent()) {
            return authenticatedUser;
        } else {
            if (!authenticatedUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            } else {
                throw new AccessDeniedException("You are not authorized to see this user.");
            }

        }
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PutMapping("/edit")
    @PreAuthorize("hasAnyAuthority('USER_ROLES', 'ADMIN_ROLES')")
    public User editUser(@RequestBody @Valid UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> authenticatedUser = userService.getUser(userDTO.getId());

        if (userService.getUser(authentication.getName()).getId() == userDTO.getId() && authenticatedUser.isPresent()) {
            return userService.editUser(userDTO);
        } else {
            if (!authenticatedUser.isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            } else {
                throw new AccessDeniedException("You are not authorized to edit this user.");
            }
        }
    }


}
