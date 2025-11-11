package com.ateam.calmate.dietManagement.command.repository;

import com.ateam.calmate.dietManagement.command.entity.ExtendFilePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExtendFilePathRepository extends JpaRepository<ExtendFilePath, Long> {
    Optional<ExtendFilePath> findByUrlPath(String urlPath);
}
