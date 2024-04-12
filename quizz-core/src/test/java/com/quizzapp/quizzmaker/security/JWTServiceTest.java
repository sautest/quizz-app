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
        String userName = "testUser";
        String roles = "ROLE_USER,ROLE_ADMIN";
        String token = jwtService.generateToken(userName, roles);

        Field secretField = JWTService.class.getDeclaredField("SECRET");
        secretField.setAccessible(true);
        String secret = (String) secretField.get(null); // for static fields, pass null


        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secret))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(userName, claims.getSubject(), "The subject should be the userName");
        assertEquals(roles, claims.get("roles"), "The roles claim should match the roles provided");

        long expectedExpirationTime = claims.getIssuedAt().getTime() + 1000 * 60 * 300;
        assertTrue(Math.abs(expectedExpirationTime - claims.getExpiration().getTime()) < 1000,
                "The token expiration time should be about 300 minutes from the issued time");
    }


    @Test
    public void testExtractUserName() {
        String expectedUserName = "testUser";
        String token = jwtService.generateToken(expectedUserName, "ROLE_USER");

        String actualUserName = jwtService.extractUserName(token);

        assertEquals(expectedUserName, actualUserName, "Extracted username should match the expected value");
    }

    @Test
    public void testExtractExpiration() {
        String userName = "testUser";
        String token = jwtService.generateToken(userName, "ROLE_USER");

        Date actualExpiration = jwtService.extractExpiration(token);

        long expectedExpirationMillis = System.currentTimeMillis() + 1000 * 60 * 300;
        long actualExpirationMillis = actualExpiration.getTime();

        assertTrue(Math.abs(expectedExpirationMillis - actualExpirationMillis) < 1000 * 60,
                "Extracted expiration should be approximately 300 minutes from the current time");
    }





}
