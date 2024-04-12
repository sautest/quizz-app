package com.quizzapp.quizzmaker.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.quizzapp.quizzmaker.dto.LoginDTO;
import com.quizzapp.quizzmaker.dto.UserDTO;
import com.quizzapp.quizzmaker.persistence.entities.AuthRequest;
import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.security.JWTService;
import com.quizzapp.quizzmaker.security.JWTServiceTest;
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
                "user@example.com",
                "testUser",
                "password123",
                false,
                "2023-01-01"
        );

        validUser = new User(
                1,
                "user@example.com",
                "testUser",
                "password123",
                false,
                "2023-01-01",
                "ROLE_USER",
                null,
                null
        );
    }

    @Test
    public void createUserTest() {
        when(userService.createUser(any(UserDTO.class))).thenReturn(validUser);
        User result = userController.createUser(userDTO);

        assertNotNull(result, "The created User should not be null");
        assertEquals(validUser.getEmail(), result.getEmail(), "Emails should match");
        assertEquals(validUser.getUsername(), result.getUsername(), "Usernames should match");

        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void login_Successful() {
        AuthRequest authRequest = new AuthRequest("user1", "password");
        User user = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser(authRequest.getUsername())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword(), Collections.emptyList()));
        when(jwtService.generateToken(eq(authRequest.getUsername()), anyString())).thenReturn("mockToken");

        ResponseEntity<Object> response = userController.addUser(authRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be OK");
        assertNotNull(response.getBody(), "Response body should not be null");
        assertTrue(response.getBody() instanceof LoginDTO, "Response body should be an instance of LoginDTO");

        verify(userService, times(3)).getUser(authRequest.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(eq(authRequest.getUsername()), anyString());
    }

    @Test
    void login_BlockedAccount() {
        AuthRequest authRequest = new AuthRequest("blockedUser", "password");
        User blockedUser = new User(2, "blockedUser@example.com", "blockedUser", "password", true, "2023-01-02", "ROLE_USER", null, null);

        when(userService.getUser(authRequest.getUsername())).thenThrow(new UsernameNotFoundException("This account is blocked."));

        ResponseEntity<Object> response = userController.addUser(authRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Status code should be FORBIDDEN");
        assertEquals("This account is blocked!", response.getBody(), "Response body should indicate blocked account");

        verify(userService).getUser(authRequest.getUsername());
    }



    @Test
    @WithMockUser(username="admin", authorities={"ADMIN_ROLES"})
    public void getAllUsersTest() {
        List<User> users = Arrays.asList(
                new User(1, "user1@example.com", "user1", "pass1", false, "2023-01-01", "ROLE_USER", null, null),
                new User(2, "user2@example.com", "user2", "pass2", false, "2023-02-01", "ROLE_USER", null, null)
        );

        when(userService.getAllUser()).thenReturn(users);

        List<User> result = userController.getAllUsers();

        assertNotNull(result, "The list of users should not be null");
        assertEquals(2, result.size(), "The list should contain 2 users");
        assertEquals("user1@example.com", result.get(0).getEmail(), "The email of the first user should match");

        verify(userService, times(1)).getAllUser();
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void editUser_Successful() {
        UserDTO userDTO = new UserDTO(1, "user1@example.com", "user1", "newpassword", false, "2023-01-01");
        User editedUser = new User(1, "user1@example.com", "user1", "newpassword", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser("user1")).thenReturn(editedUser);
        when(userService.getUser(userDTO.getId())).thenReturn(Optional.of(editedUser));
        when(userService.editUser(userDTO)).thenReturn(editedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        User result = userController.editUser(userDTO);

        assertNotNull(result, "Edited user should not be null");
        assertEquals(userDTO.getEmail(), result.getEmail(), "Emails should match after edit");

        verify(userService).getUser("user1");
        verify(userService).getUser(userDTO.getId());
        verify(userService).editUser(userDTO);

        SecurityContextHolder.clearContext();
    }


    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN_ROLES"})
    void editUser_UnauthorizedAttempt() {
        UserDTO userDTO = new UserDTO(1, "user1@example.com", "user1", "newpassword", false, "2023-01-01");
        User adminUser = new User(2, "admin@example.com", "admin", "adminpass", false, "2023-02-01", "ROLE_ADMIN", null, null);
        User targetUser = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser("admin")).thenReturn(adminUser);
        when(userService.getUser(userDTO.getId())).thenReturn(Optional.of(targetUser));

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "adminpass", Collections.singletonList(new SimpleGrantedAuthority("ADMIN_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(AccessDeniedException.class, () -> {
            userController.editUser(userDTO);
        }, "AccessDeniedException expected");

        verify(userService).getUser("admin");
        verify(userService).getUser(userDTO.getId());

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void editUser_NotFound() {
        UserDTO userDTO = new UserDTO(999, "nonexistent@example.com", "nonexistent", "password", false, "2023-01-01");
        User authenticatedUser = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser("user1")).thenReturn(authenticatedUser);
        when(userService.getUser(userDTO.getId())).thenReturn(Optional.empty());

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.clearContext();
    }


    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void getUser_AuthenticatedUser_Success() {
        int userId = 1;
        User expectedUser = new User(userId, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);
        when(userService.getUser(userId)).thenReturn(Optional.of(expectedUser));
        when(userService.getUser("user1")).thenReturn(expectedUser);

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        Optional<User> result = userController.getUser(userId);

        assertTrue(result.isPresent(), "User should be found");
        assertEquals(expectedUser.getId(), result.get().getId(), "User IDs should match");

        verify(userService).getUser(userId);
        verify(userService).getUser("user1");

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN_ROLES"})
    void getUser_AccessDeniedForDifferentUser() {
        int userId = 1;
        User adminUser = new User(2, "admin@example.com", "admin", "adminpass", false, "2023-02-01", "ROLE_ADMIN", null, null); // Different user (admin)
        User requestedUser = new User(userId, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null); // The user being requested

        when(userService.getUser(userId)).thenReturn(Optional.of(requestedUser));
        when(userService.getUser("admin")).thenReturn(adminUser);

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "adminpass", Collections.singletonList(new SimpleGrantedAuthority("ADMIN_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(AccessDeniedException.class, () -> {
            userController.getUser(userId);
        }, "AccessDeniedException expected");

        verify(userService).getUser(userId);
        verify(userService).getUser("admin");

        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"USER_ROLES"})
    void getUser_NotFound() {
        int userId = 999;
        User authenticatedUser = new User(1, "user1@example.com", "user1", "password", false, "2023-01-01", "ROLE_USER", null, null);

        when(userService.getUser(userId)).thenReturn(Optional.empty());
        when(userService.getUser("user1")).thenReturn(authenticatedUser);


        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "password", Collections.singletonList(new SimpleGrantedAuthority("USER_ROLES")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertThrows(ResponseStatusException.class, () -> {
            userController.getUser(userId);
        }, "ResponseStatusException expected");

        verify(userService).getUser(userId);
        verify(userService).getUser("user1");

        SecurityContextHolder.clearContext();
    }

}
