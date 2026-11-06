package com.vibecoders.unitime

import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

fun nextReminderTime(
    weekday: Weekday,
    classStartMinute: Int,
    leadMinutes: Int,
    now: ZonedDateTime
): ZonedDateTime {
    require(classStartMinute in 0 until 24 * 60) { "Class start time is invalid" }
    require(leadMinutes == 5 || leadMinutes == 15 || leadMinutes == 30) { "Choose 5, 15, or 30 minutes" }

    val reminderMinute = classStartMinute - leadMinutes
    val day = when (weekday) {
        Weekday.MONDAY -> java.time.DayOfWeek.MONDAY
        Weekday.TUESDAY -> java.time.DayOfWeek.TUESDAY
        Weekday.WEDNESDAY -> java.time.DayOfWeek.WEDNESDAY
        Weekday.THURSDAY -> java.time.DayOfWeek.THURSDAY
        Weekday.FRIDAY -> java.time.DayOfWeek.FRIDAY
        Weekday.SATURDAY -> java.time.DayOfWeek.SATURDAY
        Weekday.SUNDAY -> java.time.DayOfWeek.SUNDAY
    }
    var candidate = now.with(TemporalAdjusters.nextOrSame(day))
        .withHour(reminderMinute / 60)
        .withMinute(reminderMinute % 60)
        .withSecond(0)
        .withNano(0)
    if (!candidate.isAfter(now)) candidate = candidate.plusWeeks(1)
    return candidate
}
