package com.ateam.calmate.dietManagement.query.controller;

import com.ateam.calmate.dietManagement.query.dto.MealRecordResponse;
import com.ateam.calmate.dietManagement.query.service.MealRecordQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diet-management/query")
public class MealRecordQueryController {

    private final MealRecordQueryService mealRecordQueryService;

    @GetMapping
    public ResponseEntity<List<MealRecordResponse>> getMealRecords(
            @RequestParam Long memberId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String type
    ) {
        List<MealRecordResponse> responses = mealRecordQueryService.getMealRecords(memberId, date, type);
        return ResponseEntity.ok(responses);
    }
}
