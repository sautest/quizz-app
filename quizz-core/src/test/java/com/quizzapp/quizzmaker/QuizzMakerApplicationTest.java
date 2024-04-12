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

    @Mock
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        quizzMakerApplication = new QuizzMakerApplication();
    }

    @Test
    void httpClientBeanTest() {
        HttpClient httpClient = quizzMakerApplication.httpClient();
        assertNotNull(httpClient);
    }

    @Test
    void corsConfigurerTest() {
        CorsRegistry mockCorsRegistry = mock(CorsRegistry.class);
        // Mock CorsRegistration
        CorsRegistration mockCorsRegistration = mock(CorsRegistration.class);
        // Mock the return value of allowedOrigins() to avoid NullPointerException
        when(mockCorsRegistration.allowedOrigins(anyString())).thenReturn(mockCorsRegistration);
        when(mockCorsRegistry.addMapping(anyString())).thenReturn(mockCorsRegistration);

        quizzMakerApplication.corsConfigurer().addCorsMappings(mockCorsRegistry);

        verify(mockCorsRegistry).addMapping("/**");
        verify(mockCorsRegistration).allowedOrigins("*");
        verify(mockCorsRegistration).allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
    }


}
