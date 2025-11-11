package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory,Long> {
    @Query("SELECT h FROM LoginHistory h " +
            "WHERE h.date BETWEEN :start AND :end " +
            "AND h.memberId = :id")
    List<LoginHistory> findLoginHistory(@Param("start") LocalDateTime startOfDay,
                                        @Param("end") LocalDateTime endOfDay,
                                        @Param("id") Long id);

}
