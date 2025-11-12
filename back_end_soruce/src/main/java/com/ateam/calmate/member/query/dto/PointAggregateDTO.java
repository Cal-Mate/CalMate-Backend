package com.ateam.calmate.member.query.dto;

import com.ateam.calmate.member.command.entity.Point;

/**
 * Simple projection used to aggregate member point totals by distinction.
 */
public record PointAggregateDTO(Point.Distinction distinction, Long total) {
}
