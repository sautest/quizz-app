package com.quizzapp.quizzmaker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JWTServiceTest {

    private JWTService jwtService;

    @BeforeEach
    public void setUp() {
        jwtService = new JWTService();
    }

    @Test
    public void testGenerateToken() throws NoSuchFieldException, IllegalAccessException {
        String userName = "user1";
        String roles = "ROLE_USER,ROLE_ADMIN";
        String token = jwtService.generateToken(userName, roles);

        Field secretField = JWTService.class.getDeclaredField("SECRET");
        secretField.setAccessible(true);
        String secret = (String) secretField.get(null);


        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(userName, claims.getSubject());
        assertEquals(roles, claims.get("roles"));

        long expectedExpirationTime = claims.getIssuedAt().getTime() + 1000 * 60 * 300;
        assertTrue(Math.abs(expectedExpirationTime - claims.getExpiration().getTime()) < 1000);
    }


    @Test
    public void testExtractUserName() {
        String expectedUserName = "user";
        String token = jwtService.generateToken(expectedUserName, "ROLE_USER");
        String actualUserName = jwtService.extractUserName(token);

        assertEquals(expectedUserName, actualUserName);
    }

    @Test
    public void testExtractExpiration() {
        String userName = "user";
        String token = jwtService.generateToken(userName, "ROLE_USER");

        Date actualExpiration = jwtService.extractExpiration(token);

        long expectedExpirationMillis = System.currentTimeMillis() + 1000 * 60 * 300;
        long actualExpirationMillis = actualExpiration.getTime();

        assertTrue(Math.abs(expectedExpirationMillis - actualExpirationMillis) < 1000 * 60);
    }

}
