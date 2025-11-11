package com.ateam.calmate.dietManagement.command.repository;

import com.ateam.calmate.dietManagement.command.entity.FoodExtendFilePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodExtendFilePathRepository extends JpaRepository<FoodExtendFilePath, Long> {
    Optional<FoodExtendFilePath> findByUrlPath(String urlPath);
}
