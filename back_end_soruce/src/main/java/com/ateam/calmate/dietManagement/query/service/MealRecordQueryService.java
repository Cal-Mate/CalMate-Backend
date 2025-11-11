package com.ateam.calmate.dietManagement.query.service;

import com.ateam.calmate.dietManagement.query.dto.MealRecordResponse;

import java.time.LocalDate;
import java.util.List;

public interface MealRecordQueryService {

    List<MealRecordResponse> getMealRecords(Long memberId, LocalDate date, String type);
}
