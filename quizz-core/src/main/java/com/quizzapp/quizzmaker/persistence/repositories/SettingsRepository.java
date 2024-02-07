package com.quizzapp.quizzmaker.persistence.repositories;

import com.quizzapp.quizzmaker.persistence.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository  extends JpaRepository<Settings,Long> {
}
