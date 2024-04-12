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
    private JWTServiceTest jwtService;

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
    void createUser_ShouldSaveUser_WhenUserDTOIsProvided() {
        UserDTO userDTO = new UserDTO(1, "user1@example.com", "user1", "encodedPassword", false, "2024-01-01");
        User user = new User(1, "user1@example.com", "user1", "encodedPassword", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(userDTO);

        verify(passwordEncoder).encode(userDTO.getPassword());
        verify(userRepository).save(any(User.class));
        assertNotNull(createdUser);
        assertEquals(encodedPassword, createdUser.getPassword());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUsernameExists() {
        String username = "user1";
        User user = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findByUsername(username)).thenReturn(optionalUser);

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUsernameNotFound() {
        String username = "nonExistentUsername";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });
    }


    @Test
    void getAllUser_ShouldReturnAllUsers() {
        List<User> users = Arrays.asList(new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>()), new User(1, "user2@example.com", "user2", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>()));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUser();

        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    void editUser_ShouldUpdateUserDetails_WhenUserExists() {
        UserDTO userDTO = new UserDTO(1, "updated@example.com", "updatedUser", "updatedPassword", true, "2024-01-01");
        User existingUser = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());
        User updatedUser = new User(1, "updated@example.com", "updatedUser", "updatedPassword", true, "2024-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());

        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.editUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.isBlocked(), result.isBlocked());
    }

    @Test
    void editUser_ShouldThrowException_WhenUserNotFound() {
        UserDTO userDTO = new UserDTO(99, "user1@example.com", "user1", "password", false, "2024-01-01");
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.editUser(userDTO));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        int userId = 1;
        Optional<User> user = Optional.of(new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>()));
        when(userRepository.findById(userId)).thenReturn(user);

        Optional<User> result = userService.getUser(userId);

        assertTrue(result.isPresent());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenUserNotFound() {
        int userId = 99;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUser(userId);

        assertFalse(result.isPresent());
    }

    @Test
    void getUserByUsername_ShouldReturnUser_WhenUsernameExists() {
        String username = "user1";
        User user = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", new ArrayList<>(), new ArrayList<>());;
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.getUser(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void getUserByUsername_ShouldThrowException_WhenUsernameNotFound() {
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUser(username));
    }


}
