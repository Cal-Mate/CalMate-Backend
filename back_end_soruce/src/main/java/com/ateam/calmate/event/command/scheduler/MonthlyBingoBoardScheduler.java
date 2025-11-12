package com.ateam.calmate.event.command.scheduler;

import com.ateam.calmate.common.KstYearMonth;
import com.ateam.calmate.event.command.service.bingo.BingoBoardFactoryService;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

/**
 * 매월 1일 00시 30분에 모든 활성 사용자를 위한 빙고 보드를 자동 생성하는 스케줄러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyBingoBoardScheduler {

    private final BingoBoardFactoryService boardFactoryService;
    private final MemberRepository memberRepository;

    private static final int DEFAULT_BOARD_SIZE = 5;
    private static final String BOARD_TITLE_FORMAT = "%s 빙고";

    /**
     * 매월 1일 00시 30분(KST)에 실행
     * cron: "초 분 시 일 월 요일"
     * 0 30 0 1 * * = 매월 1일 00:30:00
     */
    @Scheduled(cron = "0 30 0 1 * *", zone = "Asia/Seoul")
    @Transactional
    public void createMonthlyBingoBoardsForAllUsers() {
        log.info("=== 월별 빙고 보드 자동 생성 시작 ===");

        YearMonth currentMonth = KstYearMonth.now();
        String boardTitle = String.format(BOARD_TITLE_FORMAT, currentMonth);

        try {
            // 모든 활성 회원 조회 (탈퇴하지 않은 회원만)
            List<Member> activeMembers = memberRepository.findAll().stream()
                    .filter(member -> member.getQuitDate() == null)
                    .toList();

            log.info("활성 회원 수: {}", activeMembers.size());

            int successCount = 0;
            int skipCount = 0;
            int errorCount = 0;

            for (Member member : activeMembers) {
                try {
                    // ensureMonthlyBoard: 이미 존재하면 건너뛰고, 없으면 생성
                    var board = boardFactoryService.ensureMonthlyBoard(
                            member.getId(),
                            currentMonth,
                            DEFAULT_BOARD_SIZE,
                            boardTitle
                    );

                    if (board.getCreatedAt().getMonth() == currentMonth.getMonth()) {
                        successCount++;
                        log.debug("빙고 보드 생성 완료 - memberId: {}, boardId: {}",
                                member.getId(), board.getId());
                    } else {
                        skipCount++;
                        log.debug("이미 존재하는 빙고 보드 - memberId: {}, boardId: {}",
                                member.getId(), board.getId());
                    }

                } catch (Exception e) {
                    errorCount++;
                    log.error("빙고 보드 생성 실패 - memberId: {}, error: {}",
                            member.getId(), e.getMessage(), e);
                }
            }

            log.info("=== 월별 빙고 보드 자동 생성 완료 ===");
            log.info("성공: {} / 건너뛰기: {} / 실패: {} / 전체: {}",
                    successCount, skipCount, errorCount, activeMembers.size());

        } catch (Exception e) {
            log.error("월별 빙고 보드 자동 생성 중 오류 발생", e);
        }
    }

    /**
     * 수동 실행용 메서드 (테스트 또는 관리자 기능)
     */
    public void manualCreateBingoBoardsForMonth(YearMonth targetMonth) {
        log.info("=== 수동 빙고 보드 생성 시작 - targetMonth: {} ===", targetMonth);

        String boardTitle = String.format(BOARD_TITLE_FORMAT, targetMonth);

        List<Member> activeMembers = memberRepository.findAll().stream()
                .filter(member -> member.getQuitDate() == null)
                .toList();

        for (Member member : activeMembers) {
            try {
                boardFactoryService.ensureMonthlyBoard(
                        member.getId(),
                        targetMonth,
                        DEFAULT_BOARD_SIZE,
                        boardTitle
                );
            } catch (Exception e) {
                log.error("수동 빙고 보드 생성 실패 - memberId: {}, error: {}",
                        member.getId(), e.getMessage());
            }
        }

        log.info("=== 수동 빙고 보드 생성 완료 ===");
    }
}