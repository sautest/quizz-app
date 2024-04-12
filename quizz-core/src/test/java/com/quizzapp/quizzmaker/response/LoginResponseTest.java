package com.quizzapp.quizzmaker.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoginResponseTest {

    @Test
    public void testNoArgConstructor() {
        LoginResponse response = new LoginResponse();
        assertNull(response.getMessage());
        assertNull(response.getStatus());
    }

    @Test
    public void testParamConstructor() {
        LoginResponse response = new LoginResponse("Success", true);
        assertEquals("Success", response.getMessage());
        assertEquals(Boolean.TRUE, response.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        LoginResponse response = new LoginResponse();

        response.setMessage("Failed");
        assertEquals("Failed", response.getMessage());

        response.setStatus(false);
        assertEquals(Boolean.FALSE, response.getStatus());
    }
}
