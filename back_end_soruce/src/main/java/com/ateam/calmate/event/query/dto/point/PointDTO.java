package com.ateam.calmate.event.query.dto.point;

import com.ateam.calmate.event.command.entity.point.PointEntity;
import lombok.*;

import java.time.LocalDateTime;

public class PointDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Integer point;
        private PointEntity.PointDistinction distinction;
        private Long memberId;
        private Integer diaryId;
        private Long calenderId;
        private Long gachaEventId;
        private Integer bingoBoardId;

        public PointEntity toEntity() {
            return PointEntity.builder()
                    .point(this.point)
                    .distinction(this.distinction)
                    .memberId(this.memberId)
                    .diaryId(this.diaryId)
                    .calenderId(this.calenderId)
                    .gachaEventId(this.gachaEventId)
                    .bingoBoardId(this.bingoBoardId)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long pointId;
        private Integer point;
        private PointEntity.PointDistinction distinction;
        private Long memberId;
        private Integer diaryId;
        private Long calenderId;
        private Long gachaEventId;
        private Integer bingoBoardId;
        private LocalDateTime createdAt;

        public static Response from(PointEntity point) {
            return Response.builder()
                    .pointId(point.getPointId())
                    .point(point.getPoint())
                    .distinction(point.getDistinction())
                    .memberId(point.getMemberId())
                    .diaryId(point.getDiaryId())
                    .calenderId(point.getCalenderId())
                    .gachaEventId(point.getGachaEventId())
                    .bingoBoardId(point.getBingoBoardId())
                    .createdAt(point.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long memberId;
        private Integer totalPoint;      // 총 포인트 (적립 - 사용)
        private Integer earnedPoint;     // 적립 포인트
        private Integer usedPoint;       // 사용 포인트
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private Long pointId;
        private Integer point;
        private PointEntity.PointDistinction distinction;
        private Long memberId;
        private String memberName;               // 회원명
        private Integer diaryId;
        private Long calenderId;
        private Long gachaEventId;
        private String gachaEventName;           // 뽄기 이벤트명
        private Integer bingoBoardId;
        private String bingoBoardTitle;          // 빙고판 제목
        private LocalDateTime createdAt;

        public static DetailResponse from(PointEntity point) {
            return DetailResponse.builder()
                    .pointId(point.getPointId())
                    .point(point.getPoint())
                    .distinction(point.getDistinction())
                    .memberId(point.getMemberId())
                    .diaryId(point.getDiaryId())
                    .calenderId(point.getCalenderId())
                    .gachaEventId(point.getGachaEventId())
                    .bingoBoardId(point.getBingoBoardId())
                    .createdAt(point.getCreatedAt())
                    .build();
        }
    }
}