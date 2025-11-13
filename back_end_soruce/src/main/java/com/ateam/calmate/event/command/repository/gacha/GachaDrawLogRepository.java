package com.ateam.calmate.event.command.repository.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaDrawLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GachaDrawLogRepository extends JpaRepository<GachaDrawLogEntity, Long> {

    Page<GachaDrawLogEntity> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    Page<GachaDrawLogEntity> findByMemberIdAndGachaEventIdOrderByCreatedAtDesc(Long memberId, Long gachaEventId, Pageable pageable);

    @Query("""
            select l.prizeRank as rank, count(l.id) as hitCount
            from GachaDrawLogEntity l
            where l.memberId = :memberId
              and (:eventId is null or l.gachaEventId = :eventId)
            group by l.prizeRank
            """)
    List<Object[]> countByMemberAndEventGroupByRank(@Param("memberId") Long memberId,
                                                    @Param("eventId") Long eventId);
}
