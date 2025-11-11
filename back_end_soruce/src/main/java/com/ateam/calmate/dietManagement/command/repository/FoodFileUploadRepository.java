package com.ateam.calmate.dietManagement.command.repository;

import com.ateam.calmate.dietManagement.command.entity.FoodFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodFileUploadRepository extends JpaRepository<FoodFileUpload, Long> {
}
