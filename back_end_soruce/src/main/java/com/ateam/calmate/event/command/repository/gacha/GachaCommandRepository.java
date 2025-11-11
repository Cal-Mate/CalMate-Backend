package com.ateam.calmate.event.command.repository.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaEventEntity;
import com.ateam.calmate.event.enums.EventStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GachaCommandRepository extends JpaRepository<GachaEventEntity, Long> {

    // 이벤트와 경품(컬렉션) 즉시 로딩용(수정/삭제 시 편리)
    @EntityGraph(attributePaths = {"prizes", "resetPolicy"})
    Optional<GachaEventEntity> findWithPrizesById(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update GachaEventEntity e set e.status = :status where e.id = :eventId")
    int updateStatus(Long eventId, EventStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update GachaEventEntity e set e.currentBoardVersion = :version where e.id = :eventId")
    int updateCurrentBoardVersion(Long eventId, Integer version);
}
