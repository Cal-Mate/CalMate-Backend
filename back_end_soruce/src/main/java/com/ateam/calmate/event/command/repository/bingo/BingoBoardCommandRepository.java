package com.ateam.calmate.event.command.repository.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoBoardEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface BingoBoardCommandRepository extends JpaRepository<BingoBoardEntity, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from BingoBoardEntity b where b.id = :id")
    Optional<BingoBoardEntity> findByIdForUpdate(@Param("id") Integer id);

    Optional<BingoBoardEntity> findByMemberIdAndStartDate(Long memberId, LocalDate startDate);
    boolean existsByMemberIdAndStartDate(Long memberId, LocalDate startDate);
}
