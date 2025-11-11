package com.ateam.calmate.event.command.repository.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoCellEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BingoCellCommandRepository extends JpaRepository<BingoCellEntity, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from BingoCellEntity c join fetch c.board where c.id = :id")
    Optional<BingoCellEntity> findByIdForUpdate(@Param("id") Integer id);

    @Query("select c from BingoCellEntity c where c.board.id = :boardId")
    List<BingoCellEntity> findAllByBoardId(@Param("boardId") Integer boardId);
}
