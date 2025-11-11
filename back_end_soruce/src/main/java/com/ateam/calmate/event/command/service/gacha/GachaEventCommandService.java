package com.ateam.calmate.event.command.service.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaEventEntity;
import com.ateam.calmate.event.command.entity.gacha.GachaPrizeEntity;
import com.ateam.calmate.event.command.entity.gacha.GachaQuantityEntity;
import com.ateam.calmate.event.command.entity.gacha.GachaResetEntity;
import com.ateam.calmate.event.command.repository.gacha.GachaCommandRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaPrizeRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaQuantityRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaResetRepository;
import com.ateam.calmate.event.enums.EventStatus;
import com.ateam.calmate.event.enums.PrizeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GachaEventCommandService {

    private final GachaCommandRepository eventRepo;
    private final GachaPrizeRepository prizeRepo;
    private final GachaQuantityRepository qtyRepo;
    private final GachaResetRepository resetRepo;

    /* =============== Event =============== */

    @Transactional
    public Long createEvent(LocalDateTime startAt,
                            LocalDateTime endAt,
                            Integer point,
                            EventStatus status,
                            Long resetPolicyId,
                            Integer currentBoardVersion // null 가능
    ) {
        GachaEventEntity event = GachaEventEntity.builder()
                .startAt(startAt)
                .endAt(endAt)
                .point(point)
                .status(status)
                .currentBoardVersion(currentBoardVersion)
                .build();

        if (resetPolicyId != null) {
            GachaResetEntity policy = resetRepo.findById(resetPolicyId)
                    .orElseThrow(() -> new IllegalArgumentException("reset policy not found: " + resetPolicyId));
            event.setResetPolicy(policy);
        }
        return eventRepo.save(event).getId();
    }

    @Transactional
    public void updateEvent(Long eventId,
                            LocalDateTime startAt,
                            LocalDateTime endAt,
                            Integer point,
                            EventStatus status,
                            Integer currentBoardVersion,
                            Long resetPolicyId // null이면 유지
    ) {
        GachaEventEntity event = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("event not found: " + eventId));

        if (startAt != null) event.setStartAt(startAt);
        if (endAt != null) event.setEndAt(endAt);
        if (point != null) event.setPoint(point);
        if (status != null) event.setStatus(status);
        if (currentBoardVersion != null) event.setCurrentBoardVersion(currentBoardVersion);

        if (resetPolicyId != null) {
            GachaResetEntity policy = resetRepo.findById(resetPolicyId)
                    .orElseThrow(() -> new IllegalArgumentException("reset policy not found: " + resetPolicyId));
            event.setResetPolicy(policy);
        }
        // dirty checking
    }

    @Transactional
    public void changeStatus(Long eventId, EventStatus status) {
        if (eventRepo.updateStatus(eventId, status) == 0) {
            throw new IllegalArgumentException("event not found: " + eventId);
        }
    }

    @Transactional
    public void setCurrentBoardVersion(Long eventId, Integer version) {
        if (eventRepo.updateCurrentBoardVersion(eventId, version) == 0) {
            throw new IllegalArgumentException("event not found: " + eventId);
        }
    }

    @Transactional
    public void setResetPolicy(Long eventId, Long resetPolicyId) {
        GachaEventEntity event = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("event not found: " + eventId));
        GachaResetEntity policy = resetRepo.findById(resetPolicyId)
                .orElseThrow(() -> new IllegalArgumentException("reset policy not found: " + resetPolicyId));
        event.setResetPolicy(policy);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        // prizes orphanRemoval=true라면 event만 삭제해도 prize 자동 삭제
        eventRepo.deleteById(eventId);
    }

    /* =============== Prize =============== */

    @Transactional
    public Long addPrize(Long eventId,
                         String name,
                         String payloadJson,
                         PrizeType prizeType,
                         Integer rank,
                         Integer initialQuantity // null이면 수량 엔트리 미생성
    ) {
        GachaEventEntity event = eventRepo.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("event not found: " + eventId));

        GachaPrizeEntity prize = GachaPrizeEntity.builder()
                .name(name)
                .payloadJson(payloadJson)
                .prizeType(prizeType)
                .rank(rank)
                .event(event)
                .build();

        prize = prizeRepo.save(prize);

        if (initialQuantity != null) {
            // shared PK: quantity.id == prize.id
            GachaQuantityEntity q = GachaQuantityEntity.of(prize, initialQuantity);
            qtyRepo.save(q);
            prize.attachQuantity(q); // 양방향 일관성
        }

        // 이벤트-경품 양방향 연결 보강
        event.addPrize(prize);
        return prize.getId();
    }

    @Transactional
    public void updatePrize(Long prizeId,
                            String name,
                            String payloadJson,
                            PrizeType prizeType,
                            Integer rank,
                            Integer quantity // null이면 수량 변경 없음
    ) {
        GachaPrizeEntity prize = prizeRepo.findById(prizeId)
                .orElseThrow(() -> new IllegalArgumentException("prize not found: " + prizeId));

        if (name != null) prize.setName(name);
        if (payloadJson != null) prize.setPayloadJson(payloadJson);
        if (prizeType != null) prize.setPrizeType(prizeType);
        if (rank != null) prize.setRank(rank);

        if (quantity != null) {
            GachaQuantityEntity q = prize.getQuantity();
            if (q == null) {
                q = GachaQuantityEntity.of(prize, quantity);
                qtyRepo.save(q);
                prize.attachQuantity(q);
            } else {
                q.setCount(quantity);
                // dirty checking
            }
        }
    }

    @Transactional
    public void deletePrize(Long prizeId) {
        // quantity는 shared PK 관계이므로 cascade/orphan으로 함께 제거됨
        prizeRepo.deleteById(prizeId);
    }
}