package com.ateam.calmate.ai.command.service;

import com.ateam.calmate.ai.command.dto.AiRequestDTO;
import com.ateam.calmate.ai.command.dto.GoalType;
import com.ateam.calmate.ai.command.dto.RequestDietDTO;
import com.ateam.calmate.ai.command.dto.ResponseDietDTO;
import com.ateam.calmate.ai.command.repository.AiDietRepository;
import com.ateam.calmate.ai.query.dto.GoalQueryDTO;
import com.ateam.calmate.ai.query.service.AiDietQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AiDietCommandService {

    private final AiDietRepository aiDietRepository;
    private final AiDietQueryService aiDietQueryService;

    @Autowired
    public AiDietCommandService(AiDietRepository aiDietRepository,
                                AiDietQueryService aiDietQueryService) {
        this.aiDietRepository = aiDietRepository;
        this.aiDietQueryService = aiDietQueryService;
    }

    public ResponseDietDTO getDietAndSave(RequestDietDTO request) {
        // AI 전해줄 DTO 만드는 과정 = 목표, 목표 몸무게, 알레르기 불러오기
        AiRequestDTO sendRequest = makeAiRequest(request);
        log.info("sendRequest={}", sendRequest);
        return new ResponseDietDTO();

        // Fast API로 통신

        // AI 응답으로 온 Json을 편집

        // ai_diet 테이블에 응답을 저장

        // ResponseEntity에 담기

    }

    private AiRequestDTO makeAiRequest(RequestDietDTO request) {
        AiRequestDTO aiRequest = new AiRequestDTO();
        aiRequest.setGender(request.getGender());
        aiRequest.setHeight(request.getHeight());
        aiRequest.setWeight(request.getWeight());
        GoalQueryDTO result = aiDietQueryService.getMemberGoalInfo(request.getMemberId());
        aiRequest.setGoalType(result.getGoalType());
        aiRequest.setTargetValue(result.getTargetValue());
        aiRequest.setStartDate(result.getStartDate());
        aiRequest.setEndDate(result.getEndDate());
        aiRequest.setBodyMetric(request.getBodyMetric());
        List<String> allergyNames = aiDietQueryService.getAllergy(request.getMemberId());
        return aiRequest;

    }


}
