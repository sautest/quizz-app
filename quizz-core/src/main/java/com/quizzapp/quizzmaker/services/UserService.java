package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService  {

    User createUser(UserDTO userDTO);

    UserDetails loadUserByUsername(String userName);

    User getUser(Integer id);
    
    User getUser(String  username);

    List<User> getAllUser();

    User editUser(UserDTO userDTO);


}
