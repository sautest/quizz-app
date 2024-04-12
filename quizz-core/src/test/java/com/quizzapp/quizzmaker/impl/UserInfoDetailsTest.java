package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.persistence.entities.User;
import com.quizzapp.quizzmaker.services.impl.UserInfoDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInfoDetailsTest {

    private UserInfoDetails userDetails;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRoles("ROLE_USER,ROLE_ADMIN");
        userDetails = new UserInfoDetails(user);
    }

    @Test
    void getAuthorities_ShouldReturnCorrectAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void getPassword_ShouldReturnCorrectPassword() {
        assertEquals("testPassword", userDetails.getPassword());
    }

    @Test
    void getUsername_ShouldReturnCorrectUsername() {
        assertEquals("testUser", userDetails.getUsername());
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        assertTrue(userDetails.isEnabled());
    }
}
