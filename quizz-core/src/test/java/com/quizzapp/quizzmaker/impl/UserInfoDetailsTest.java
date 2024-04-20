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
        user.setUsername("user1");
        user.setPassword("password2");
        user.setRoles("ROLE_USER,ROLE_ADMIN");
        userDetails = new UserInfoDetails(user);
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testGetPassword() {
        assertEquals("password2", userDetails.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals("user1", userDetails.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userDetails.isEnabled());
    }
}
