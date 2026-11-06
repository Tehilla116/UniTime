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

    val firstClassDay = now.with(TemporalAdjusters.nextOrSame(weekday.toJavaDayOfWeek())).toLocalDate()
    var classDate = firstClassDay.atTime(classStartMinute / 60, classStartMinute % 60).atZone(now.zone)
    var reminderAt = classDate.minusMinutes(leadMinutes.toLong())
    if (!reminderAt.isAfter(now)) {
        classDate = classDate.plusWeeks(1)
        reminderAt = classDate.minusMinutes(leadMinutes.toLong())
    }
    return reminderAt
}

private fun Weekday.toJavaDayOfWeek(): java.time.DayOfWeek = when (this) {
    Weekday.MONDAY -> java.time.DayOfWeek.MONDAY
    Weekday.TUESDAY -> java.time.DayOfWeek.TUESDAY
    Weekday.WEDNESDAY -> java.time.DayOfWeek.WEDNESDAY
    Weekday.THURSDAY -> java.time.DayOfWeek.THURSDAY
    Weekday.FRIDAY -> java.time.DayOfWeek.FRIDAY
    Weekday.SATURDAY -> java.time.DayOfWeek.SATURDAY
    Weekday.SUNDAY -> java.time.DayOfWeek.SUNDAY
}
