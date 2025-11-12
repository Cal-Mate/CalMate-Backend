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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        String fastApiUrl = "http://localhost:8000/api/v1/diet/recommend";
        log.info("AI 서버({})에 식단 추천을 요청합니다...", fastApiUrl);

        // AI 응답으로 온 Json을 편집
        AiResponseDTO aiResponse = restTemplate.postForObject(
                fastApiUrl,     // 요청 URL
                sendRequest,    // 보낼 객체 (자동으로 JSON이 됩니다)
                AiResponseDTO.class // 응답받을 객체 타입
        );

        // 오늘자 기존 데이터가 존재한다면 삭제
        purgeTodayDiet(memberId);

        // ai_diet 테이블에 응답을 저장
        List<AiDietEntity> entitiesToSave = convertDtoToEntities(aiResponse, memberId);
        aiDietRepository.saveAll(entitiesToSave);
        log.info("AI 식단이 DB에 성공적으로 저장되었습니다.");


        // AiResponse 반환하기
        return aiResponse;
    }

    private void purgeTodayDiet(BigInteger memberId) {
        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(zone);
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = today.plusDays(1).atStartOfDay().minusNanos(1);

        List<Integer> ids = aiDietQueryService.findTodayDietIds(memberId, start, end);

        if (ids != null && !ids.isEmpty()) {
            aiDietRepository.deleteAllByIdInBatch(ids);
            log.info("기존 식단 {}건 삭제(회원:{}, 기간:{}~{})", ids.size(), memberId, start, end);
        } else {
            log.info("삭제할 기존 식단 없음(회원:{}, 날짜:{})", memberId, today);
        }
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


    public AiExerciseResponseDTO getExercise(RequestExerciseDTO request) {
        BigInteger memberId = request.getMemberId();
        // AI 전해줄 DTO 만드는 과정 = 목표, 목표 몸무게, 알레르기 불러오기
        AiExerciseRequestDTO exerciseRequest = makeAiExerciseRequest(request);
        log.info("sendExerciseRequest={}", exerciseRequest);

        // Fast API로 통신
        String fastApiUrl = "http://localhost:8000/api/v1/exercise/recommend";
        log.info("AI 서버({})에 운동 추천을 요청합니다...", fastApiUrl);

        // AI 응답으로 온 Json을 편집
        AiExerciseResponseDTO exerciseResponse = restTemplate.postForObject(
                fastApiUrl,     // 요청 URL
                exerciseRequest,    // 보낼 객체 (자동으로 JSON이 됩니다)
                AiExerciseResponseDTO.class // 응답받을 객체 타입
        );
        // AiResponse 반환하기
        return exerciseResponse;
    }

    private AiExerciseRequestDTO makeAiExerciseRequest(RequestExerciseDTO request) {
        AiExerciseRequestDTO exerciseRequest = new AiExerciseRequestDTO();
        exerciseRequest.setGender(request.getGender());
        exerciseRequest.setHeight(request.getHeight());
        exerciseRequest.setWeight(request.getWeight());
        GoalQueryDTO result = aiDietQueryService.getMemberGoalInfo(request.getMemberId());
        exerciseRequest.setGoalType(result.getGoalType());
        exerciseRequest.setTargetValue(result.getTargetValue());
        exerciseRequest.setStartDate(result.getStartDate());
        exerciseRequest.setEndDate(result.getEndDate());
        exerciseRequest.setBodyMetric(request.getBodyMetric());

        return exerciseRequest;
    }
}
