package com.ateam.calmate.member.common;

import lombok.Getter;

@Getter
public enum MemberGrade {
    STARTER("스타터", 0, 20),
    CHALLENGER("챌린저", 21, 50),
    FIGHTER("파이터", 51, 150),
    PRO("프로", 151, 364),
    CHAMPION("챔피언", 365, Integer.MAX_VALUE);

    private final String label;
    private final int min;
    private final int max;

    MemberGrade(String label, int min, int max) {
        this.label = label;
        this.min = min;
        this.max = max;
    }

    public static MemberGrade fromBadgeCount(int badgeCount) {
        for (MemberGrade g : values()) {
            if (badgeCount >= g.min && badgeCount <= g.max) {
                return g;
            }
        }
        return STARTER;
    }
}
