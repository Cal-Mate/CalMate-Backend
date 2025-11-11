package com.ateam.calmate.dietManagement.query.service;

import com.ateam.calmate.dietManagement.query.dto.MealFileResponse;
import com.ateam.calmate.dietManagement.query.dto.MealFoodResponse;
import com.ateam.calmate.dietManagement.query.dto.MealRecordResponse;
import com.ateam.calmate.dietManagement.query.dto.MealRecordRow;
import com.ateam.calmate.dietManagement.query.mapper.MealRecordQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MealRecordQueryServiceImpl implements MealRecordQueryService {

    private final MealRecordQueryMapper mealRecordQueryMapper;

    @Override
    public List<MealRecordResponse> getMealRecords(Long memberId, LocalDate date, String type) {
        List<MealRecordRow> rows = mealRecordQueryMapper.selectMealRecords(memberId, date, type);

        Map<Long, MealRecordResponse> map = new HashMap<>();

        for (MealRecordRow row : rows) {
            MealRecordResponse meal = map.get(row.getMealId());
            if (meal == null) {
                MealRecordResponse created = MealRecordResponse.builder()
                        .mealId(row.getMealId())
                        .date(row.getDate())
                        .type(row.getType())
                        .createdAt(row.getCreatedAt())
                        .memberId(row.getMemberId())
                        .build();

                if (row.getFoodId() != null) {
                    MealFoodResponse food = MealFoodResponse.builder()
                            .foodId(row.getFoodId())
                            .name(row.getFoodName())
                            .gram(row.getGram())
                            .kcal(row.getKcal())
                            .carbo(row.getCarbo())
                            .protein(row.getProtein())
                            .fat(row.getFat())
                            .sodium(row.getSodium())
                            .build();
                    created.setFood(food);
                }

                created.setFiles(new ArrayList<>());
                map.put(row.getMealId(), created);
                meal = created;
            }

            if (row.getFileId() != null) {
                MealFileResponse file = MealFileResponse.builder()
                        .fileId(row.getFileId())
                        .fileName(row.getFileName())
                        .fileUrl(row.getFileUrl())
                        .thumbUrl(row.getThumbUrl())
                        .uploadOrder(row.getUploadOrder())
                        .build();
                meal.getFiles().add(file);
            }
        }

        return new ArrayList<>(map.values());
    }
}
