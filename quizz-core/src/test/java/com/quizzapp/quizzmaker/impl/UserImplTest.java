package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.persistence.repositories.UserRepository;
import com.quizzapp.quizzmaker.security.JWTServiceTest;
import com.quizzapp.quizzmaker.services.impl.UserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class UserImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        UserDTO userDTO = new UserDTO(1, "user1@gmail.com", "user1", "password1", false, "2024-01-01");
        User user = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());
        String encodedPassword = "password1";

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser);
        assertEquals(encodedPassword, createdUser.getPassword());

        verify(passwordEncoder).encode(userDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLoadUserByUsername() {
        String username = "user1";
        User user = new User(1, "user1@gmail.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(optionalUser);

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsernameException() {
        String username = "bla bla bla";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });
    }


    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(new User(1, "user1@gmail.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>()), new User(1, "user2@gmail.com", "user2", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>()));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUser();

        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void testEditUser() {
        UserDTO userDTO = new UserDTO(1, "user2@gmail.com", "user2", "password2", true, "2024-01-01");
        User existingUser = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());
        User updatedUser = new User(1, "user2@gmail.com", "user2", "password2", true, "2024-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.editUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.isBlocked(), result.isBlocked());
    }

    @Test
    void testEditUserException() {
        UserDTO userDTO = new UserDTO(99, "user1@gmail.com", "user1", "password1", false, "2024-01-01");
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.editUser(userDTO));
    }

    @Test
    void testGetUserById() {
        int userId = 1;
        Optional<User> user = Optional.of(new User(1, "user1@gmail.com", "user1", "password1", false, "2025-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>()));
        when(userRepository.findById(userId)).thenReturn(user);

        Optional<User> result = userService.getUser(userId);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetUserByIdeException() {
        int userId = 944;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetUserByUsername() {
        String username = "user1";
        User user = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());;
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.getUser(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetUserByUsernameException() {
        String username = "user2141";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUser(username));
    }


}
