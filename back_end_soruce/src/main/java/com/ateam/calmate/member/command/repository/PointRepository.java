package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.Point;
import com.ateam.calmate.member.query.dto.PointAggregateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Integer> {

    @Query("""
        SELECT new com.ateam.calmate.member.query.dto.PointAggregateDTO(p.distinction, COALESCE(SUM(p.point), 0))
        FROM Point p
        WHERE p.memberId = :memberId
        GROUP BY p.distinction
        """)
    List<PointAggregateDTO> sumByMemberId(@Param("memberId") Long memberId);

    Page<Point> findByMemberIdOrderByPointIdDesc(Long memberId, Pageable pageable);
}
