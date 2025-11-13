package com.ateam.calmate.event.command.repository.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoFileUploadEntity;
import org.springframework.data.jpa.repository.*;

public interface BingoFileUploadCommandRepository extends JpaRepository<BingoFileUploadEntity, Integer> {}