package com.quizzapp.quizzmaker.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.quizzapp.quizzmaker.dto.LoginDTO;
import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.AuthRequest;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.security.JWTService;
import com.quizzapp.quizzmaker.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserController userController;

    private UserDTO userDTO;
    private User validUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO(
                0,
                "user1@gmail.com",
                "user1",
                "password123",
                false,
                "2024-01-01"
        );

        validUser = new User(
                1,
                "user1@gmail.com",
                "user1",
                "password123",
                false,
                "2024-01-01",
                "ROLE_USER",
                null,
                null
        );
    }

    @Test
    public void testCreateUser() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(validUser);
        User result = userController.createUser(userDTO);

        assertNotNull(result);
        assertEquals(validUser.getEmail(), result.getEmail());
        assertEquals(validUser.getUsername(), result.getUsername());

        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    void testLogin() {
        AuthRequest authRequest = new AuthRequest("user1", "password1");
        User user = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser(authRequest.getUsername())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword(), Collections.emptyList()));
        when(jwtService.generateToken(eq(authRequest.getUsername()), anyString())).thenReturn("token");

        ResponseEntity<Object> response = userController.addUser(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LoginDTO);

        verify(userService, times(3)).getUser(authRequest.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(eq(authRequest.getUsername()), anyString());
    }

    @Test
    void testLoginBlocked() {
        AuthRequest authRequest = new AuthRequest("user1", "password1");
        User blockedUser = new User(2, "user1@gmail.com", "user1", "password1", true, "2023-01-02", "ROLE_USER", null, null);

        when(userService.getUser(authRequest.getUsername())).thenThrow(new UsernameNotFoundException("This account is blocked!"));

        ResponseEntity<Object> response = userController.addUser(authRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("This account is blocked!", response.getBody());

        verify(userService).getUser(authRequest.getUsername());
    }



    @Test
    @WithMockUser(username="admin", authorities={"ADMIN_ROLES"})
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", null, null),
                new User(2, "user2@gmail.com", "user2", "password2", false, "2023-02-01", "ROLE_USER", null, null)
        );

        when(userService.getAllUser()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@gmail.com", result.get(0).getEmail());

        verify(userService).getAllUser();
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void testEditUser() {
        UserDTO userDTO = new UserDTO(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01");
        User editedUser = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser("user1")).thenReturn(editedUser);
        when(userService.getUser(userDTO.getId())).thenReturn(Optional.of(editedUser));
        when(userService.editUser(userDTO)).thenReturn(editedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User result = userController.editUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getEmail(), result.getEmail());

        verify(userService).getUser("user1");
        verify(userService).getUser(userDTO.getId());
        verify(userService).editUser(userDTO);

        SecurityContextHolder.clearContext();
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN_ROLES"})
    void testEditUserException() {
        UserDTO userDTO = new UserDTO(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01");
        User adminUser = new User(2, "admin@gmail.com", "admin", "password1", false, "2023-02-01", "ROLE_ADMIN", null, null);
        User targetUser = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser("admin")).thenReturn(adminUser);
        when(userService.getUser(userDTO.getId())).thenReturn(Optional.of(targetUser));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password1", Collections.singletonList(new SimpleGrantedAuthority("ADMIN_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(AccessDeniedException.class, () -> {userController.editUser(userDTO);});

        verify(userService).getUser("admin");
        verify(userService).getUser(userDTO.getId());

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void testEditUserNotFound() {
        UserDTO userDTO = new UserDTO(999, "user2@gmail.com", "user2", "password2", false, "2023-01-01");
        User authenticatedUser = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser("user1")).thenReturn(authenticatedUser);
        when(userService.getUser(userDTO.getId())).thenReturn(Optional.empty());

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.clearContext();
    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void testGetUser() {
        int userId = 1;
        User expectedUser = new User(userId, "user1@gmail.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);
        when(userService.getUser(userId)).thenReturn(Optional.of(expectedUser));
        when(userService.getUser("user1")).thenReturn(expectedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        Optional<User> result = userController.getUser(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUser.getId(), result.get().getId());

        verify(userService).getUser(userId);
        verify(userService).getUser("user1");

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN_ROLES"})
    void testGetUserException() {
        int userId = 1;
        User adminUser = new User(2, "admin@gmail.com", "admin", "password1", false, "2023-02-01", "ROLE_ADMIN", null, null);
        User requestedUser = new User(userId, "user1@gmail.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser(userId)).thenReturn(Optional.of(requestedUser));
        when(userService.getUser("admin")).thenReturn(adminUser);

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password1", Collections.singletonList(new SimpleGrantedAuthority("ADMIN_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(AccessDeniedException.class, () -> {userController.getUser(userId);});

        verify(userService).getUser(userId);
        verify(userService).getUser("admin");

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void testGetUserNotFound() {
        int userId = 999;
        User authenticatedUser = new User(1, "user1@gmail.com", "user1", "password1", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser(userId)).thenReturn(Optional.empty());
        when(userService.getUser("user1")).thenReturn(authenticatedUser);


        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password1", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(ResponseStatusException.class, () -> {userController.getUser(userId);});

        verify(userService).getUser(userId);
        verify(userService).getUser("user1");

        SecurityContextHolder.clearContext();
    }

}
