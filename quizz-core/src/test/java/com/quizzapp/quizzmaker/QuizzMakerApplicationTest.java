package com.quizzapp.quizzmaker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class QuizzMakerApplicationTests {

    private QuizzMakerApplication quizzMakerApplication;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        quizzMakerApplication = new QuizzMakerApplication();
    }

    @Test
    void testHttpClientBean() {
        HttpClient httpClient = quizzMakerApplication.httpClient();
        assertNotNull(httpClient);
    }

    @Test
    void testCorsConfigurer() {
        CorsRegistry mockCorsRegistry = mock(CorsRegistry.class);
        CorsRegistration mockCorsRegistration = mock(CorsRegistration.class);
        when(mockCorsRegistration.allowedOrigins(anyString())).thenReturn(mockCorsRegistration);
        when(mockCorsRegistry.addMapping(anyString())).thenReturn(mockCorsRegistration);

        quizzMakerApplication.corsConfigurer().addCorsMappings(mockCorsRegistry);

        verify(mockCorsRegistry).addMapping("/**");
        verify(mockCorsRegistration).allowedOrigins("*");
        verify(mockCorsRegistration).allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
    }


}
