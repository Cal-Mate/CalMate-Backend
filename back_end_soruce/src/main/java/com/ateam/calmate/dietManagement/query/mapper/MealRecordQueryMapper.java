package com.ateam.calmate.dietManagement.query.mapper;

import com.ateam.calmate.dietManagement.query.dto.MealRecordRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MealRecordQueryMapper {

    List<MealRecordRow> selectMealRecords(
            @Param("memberId") Long memberId,
            @Param("date") LocalDate date,
            @Param("type") String type
    );
}
