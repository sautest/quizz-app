package com.quizzapp.quizzmaker.controller;

import com.quizzapp.quizzmaker.persistence.entities.Health;
import com.quizzapp.quizzmaker.services.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/healthTest")
@RequiredArgsConstructor
public class HealthCheckController {
    @Autowired
    private final HealthService service;
    @GetMapping
    public List<Health> getHealth(){
        return service.getHealth();
    }
}
