package com.quizzapp.quizzmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpClient;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class QuizzMakerApplication {

	// shortcuts
	// comment ctrl+/
	// format code ctrl+alt+l

	@Bean
	public HttpClient httpClient() {
		return HttpClient.newBuilder().build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(QuizzMakerApplication.class, args);
	}

}
