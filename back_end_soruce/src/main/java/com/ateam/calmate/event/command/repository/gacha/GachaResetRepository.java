package com.ateam.calmate.event.command.repository.gacha;

import com.ateam.calmate.event.command.entity.gacha.GachaResetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GachaResetRepository extends JpaRepository<GachaResetEntity, Long> {}
