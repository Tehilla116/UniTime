package com.vibecoders.unitime

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Test

class ReminderTimeTest {
    @Test
    fun schedulesBeforeNextMatchingWeekdayClass() {
        val zone = ZoneId.of("UTC")
        val now = ZonedDateTime.of(LocalDateTime.parse("2026-11-06T08:00:00"), zone)

        val scheduled = nextReminderTime(Weekday.MONDAY, classStartMinute = 9 * 60, leadMinutes = 15, now = now)

        assertEquals(DayOfWeek.MONDAY, scheduled.dayOfWeek)
        assertEquals(8, scheduled.hour)
        assertEquals(45, scheduled.minute)
    }

    @Test
    fun ifReminderTimeAlreadyPassedTodaySchedulesForNextWeek() {
        val zone = ZoneId.of("UTC")
        val now = ZonedDateTime.of(LocalDateTime.parse("2026-11-06T08:50:00"), zone)

        val scheduled = nextReminderTime(Weekday.FRIDAY, classStartMinute = 9 * 60, leadMinutes = 15, now = now)

        assertEquals(DayOfWeek.FRIDAY, scheduled.dayOfWeek)
        assertEquals(13, scheduled.dayOfMonth)
        assertEquals(8, scheduled.hour)
        assertEquals(45, scheduled.minute)
    }
}
