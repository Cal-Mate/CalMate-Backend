package com.ateam.calmate.ai.command.service;

import com.ateam.calmate.ai.command.dto.*;
import com.ateam.calmate.ai.command.entity.AiDietEntity;
import com.ateam.calmate.ai.command.repository.AiDietRepository;
import com.ateam.calmate.ai.query.dto.GoalQueryDTO;
import com.ateam.calmate.ai.query.service.AiDietQueryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AiDietCommandService {

    private final AiDietRepository aiDietRepository;
    private final RestTemplate restTemplate;
    private final AiDietQueryService aiDietQueryService;

    @Autowired
    public AiDietCommandService(AiDietRepository aiDietRepository, RestTemplate restTemplate,
                                AiDietQueryService aiDietQueryService) {
        this.aiDietRepository = aiDietRepository;
        this.restTemplate = restTemplate;
        this.aiDietQueryService = aiDietQueryService;
    }

    public AiResponseDTO getDietAndSave(RequestDietDTO request) {
        BigInteger memberId = request.getMemberId();
        // AI 전해줄 DTO 만드는 과정 = 목표, 목표 몸무게, 알레르기 불러오기
        AiRequestDTO sendRequest = makeAiRequest(request);
        log.info("sendRequest={}", sendRequest);

        // Fast API로 통신
        String fastApiUrl = "http://localhost:8000/api/v1/diet/recommend"; // (예시 주소)
        log.info("AI 서버({})에 식단 추천을 요청합니다...", fastApiUrl);

        // AI 응답으로 온 Json을 편집
        AiResponseDTO aiResponse = restTemplate.postForObject(
                fastApiUrl,     // 요청 URL
                sendRequest,    // 보낼 객체 (자동으로 JSON이 됩니다)
                AiResponseDTO.class // 응답받을 객체 타입
        );
        log.info("aiResponse={}", aiResponse.toString());

        // ai_diet 테이블에 응답을 저장
        List<AiDietEntity> entitiesToSave = convertDtoToEntities(aiResponse, memberId);
        aiDietRepository.saveAll(entitiesToSave);
        log.info("AI 식단이 DB에 성공적으로 저장되었습니다.");


        // AiResponse 반환하기
        return aiResponse;
    }

    private List<AiDietEntity> convertDtoToEntities(AiResponseDTO aiResponse, BigInteger memberId) {
        List<AiDietEntity> entities = new ArrayList<>();

        AiResponseDTO.PlanDetailsDTO plan = aiResponse.getPlanDetails();

        if (plan == null) {
            log.warn("AI 응답에 'plan_details'가 비어있습니다.");
            return entities;
        }

        if (plan.getBreakfast() != null && plan.getBreakfast().getItems() != null) {
            AiResponseDTO.MealDTO meal = plan.getBreakfast(); // 아침 식사(요약 정보 포함)
            for (AiResponseDTO.ItemDTO item : meal.getItems()) {
                entities.add(createDietEntity(memberId, MealType.BREAKFAST, item, meal));
            }
        }

        if (plan.getLunch() != null && plan.getLunch().getItems() != null) {
            AiResponseDTO.MealDTO meal = plan.getLunch();
            for (AiResponseDTO.ItemDTO item : meal.getItems()) {
                entities.add(createDietEntity(memberId, MealType.LUNCH, item, meal));
            }
        }

        if (plan.getDinner() != null && plan.getDinner().getItems() != null) {
            AiResponseDTO.MealDTO meal = plan.getDinner();
            for (AiResponseDTO.ItemDTO item : meal.getItems()) {
                entities.add(createDietEntity(memberId, MealType.DINNER, item, meal));
            }
        }

        if (plan.getSnack() != null && plan.getSnack().getItems() != null) {
            AiResponseDTO.MealDTO meal = plan.getSnack();
            for (AiResponseDTO.ItemDTO item : meal.getItems()) {
                entities.add(createDietEntity(memberId, MealType.SNACK, item, meal));
            }
        }

        return entities;
    }

    private AiDietEntity createDietEntity(BigInteger memberId, MealType type,
                                          AiResponseDTO.ItemDTO item, AiResponseDTO.MealDTO meal) {
        AiDietEntity entity = new AiDietEntity();
        entity.setMemberId(memberId);
        entity.setType(type);
        entity.setName(item.getMenuName());
        entity.setKcal(item.getKcal());
        entity.setTotalKcal(meal.getTotalKcal());
        entity.setTotalProtein(meal.getTotalProteinG());
        entity.setTotalFat(meal.getTotalFatG());
        return entity;
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
        aiRequest.setAllergyNames(allergyNames);
        return aiRequest;
    }


}
