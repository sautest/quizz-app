package com.quizzapp.quizzmaker.services.impl;

import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.List;
import java.util.Optional;

@Service
public class UserImpl implements UserDetailsService,UserService {

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
                "USER_ROLES"
        );

        userRepository.save(user);

        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = userRepository.findByUsername(username);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"+username));
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User getUser(Integer id){
        return userRepository.findById(id).get();
    }

    @Override
    public User getUser(String username) {
        {
            return userRepository.findByUsername(username).get();
        }
    }


}
