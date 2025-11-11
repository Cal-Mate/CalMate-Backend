package com.ateam.calmate.event.command.repository.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaSharedBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GachaSharedBoardRepository extends JpaRepository<GachaSharedBoardEntity, Long> {

    List<GachaSharedBoardEntity> findAllByGachaEventIdAndBoardVersionOrderByRowAscColAsc(Long gachaEventId, Integer boardVersion);
}
