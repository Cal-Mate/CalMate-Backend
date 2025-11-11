package com.ateam.calmate.dietManagement.command.service;

import com.ateam.calmate.dietManagement.command.dto.MealRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MealCommandService {

    Long createMeal(MealRequest request, List<MultipartFile> files);

    void updateMeal(Long id, MealRequest request, List<MultipartFile> files);

    void deleteMeal(Long id);
}
