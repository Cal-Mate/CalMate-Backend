package com.ateam.calmate.event.command.repository.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaPrizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GachaPrizeRepository extends JpaRepository<GachaPrizeEntity, Long> {

    Optional<GachaPrizeEntity> findFirstByEvent_IdOrderByRankAsc(Long eventId);

    List<GachaPrizeEntity> findAllByEvent_IdOrderByRankAsc(Long eventId);
}
