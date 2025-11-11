package com.ateam.calmate.common;

import java.time.*;

public class KstYearMonth {
    public static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    public static YearMonth now() { return YearMonth.now(ZONE); }
    public static LocalDate monthStart(YearMonth ym) { return ym.atDay(1); }
    public static LocalDate monthEnd(YearMonth ym) { return ym.atEndOfMonth(); }
}