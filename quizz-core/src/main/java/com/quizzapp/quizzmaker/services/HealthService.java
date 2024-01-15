package com.quizzapp.quizzmaker.services;

import com.quizzapp.quizzmaker.persistence.entities.Health;
import com.quizzapp.quizzmaker.persistence.repositories.HealthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthService {
    @Autowired
    private HealthRepository repository;

    public  List<Health> getHealth() {
        return repository.findAll();
    }

}
