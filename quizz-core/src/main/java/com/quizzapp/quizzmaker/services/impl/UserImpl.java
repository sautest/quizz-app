package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.Quiz;
import com.quizzapp.quizzmaker.persistence.entities.Survey;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.security.JWTService;
import com.quizzapp.quizzmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserImpl implements UserDetailsService, UserService {

    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserDTO userDTO) {

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User user = new User(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                false,
                userDTO.getDateJoined(),
                "USER_ROLES", new ArrayList<Quiz>(),new ArrayList<Survey>()
        );

        userRepository.save(user);

        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = userRepository.findByUsername(username);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User editUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setBlocked(userDTO.isBlocked());

        return userRepository.save(user);

    }

    public Optional<User> getUser(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUser(String username) {
        {
            return userRepository.findByUsername(username).get();
        }
    }


}
