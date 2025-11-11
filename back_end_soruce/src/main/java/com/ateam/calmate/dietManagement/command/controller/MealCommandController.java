package com.ateam.calmate.dietManagement.command.controller;

import com.ateam.calmate.dietManagement.command.dto.MealRequest;
import com.ateam.calmate.dietManagement.command.service.MealCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diet-management")
public class MealCommandController {

    private final MealCommandService mealCommandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createMeal(
            @RequestPart("request") MealRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        Long id = mealCommandService.createMeal(request, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMeal(
            @PathVariable Long id,
            @RequestPart("request") MealRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        mealCommandService.updateMeal(id, request, files);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealCommandService.deleteMeal(id);
        return ResponseEntity.noContent().build();
    }
}
