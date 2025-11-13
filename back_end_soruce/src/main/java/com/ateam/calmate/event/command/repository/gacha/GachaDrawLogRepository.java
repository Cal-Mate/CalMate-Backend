package com.ateam.calmate.event.command.repository.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaDrawLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GachaDrawLogRepository extends JpaRepository<GachaDrawLogEntity, Long> {

    Page<GachaDrawLogEntity> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    Page<GachaDrawLogEntity> findByMemberIdAndGachaEventIdOrderByCreatedAtDesc(Long memberId, Long gachaEventId, Pageable pageable);
}
