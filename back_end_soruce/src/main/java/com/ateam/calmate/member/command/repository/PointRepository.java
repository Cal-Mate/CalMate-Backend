package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Integer> {
}
