package com.vibecoders.unitime

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
    fun rejectsUnsupportedLeadTime() {
        val now = ZonedDateTime.of(LocalDateTime.parse("2026-11-06T08:00:00"), ZoneId.of("UTC"))
        val failure = runCatching { nextReminderTime(Weekday.MONDAY, 9 * 60, 10, now) }.exceptionOrNull()
        assertTrue(failure is IllegalArgumentException)
    }

    @Test
    fun rolloverClassBeforeFirstAvailableReminderTimeMovesReminderToPreviousDay() {
        val now = ZonedDateTime.of(LocalDateTime.parse("2026-11-08T10:00:00"), ZoneId.of("UTC"))
        val reminder = nextReminderTime(Weekday.MONDAY, classStartMinute = 10, leadMinutes = 15, now = now)
        assertEquals(DayOfWeek.SUNDAY, reminder.dayOfWeek)
        assertEquals(23, reminder.hour)
        assertEquals(55, reminder.minute)
        assertEquals(8, reminder.dayOfMonth)
    }

    @Test
    fun classStartingBeforeReminderLeadWindowUsesPreviousDayForNextWeek() {
        val now = ZonedDateTime.of(LocalDateTime.parse("2026-11-06T08:00:00"), ZoneId.of("UTC"))
        val reminder = nextReminderTime(Weekday.MONDAY, classStartMinute = 10, leadMinutes = 15, now = now)
        assertEquals(DayOfWeek.SUNDAY, reminder.dayOfWeek)
        assertEquals(23, reminder.hour)
        assertEquals(55, reminder.minute)
        assertEquals(8, reminder.dayOfMonth)
    }

    @Test
    fun firstUpcomingClassDoesNotRepeatEveryWeek() {
        val now = ZonedDateTime.of(LocalDateTime.parse("2026-11-08T10:00:00"), ZoneId.of("UTC"))
        val reminder = nextReminderTime(Weekday.MONDAY, classStartMinute = 9 * 60, leadMinutes = 15, now = now)
        assertEquals(9, reminder.dayOfMonth)
        assertEquals(DayOfWeek.MONDAY, reminder.dayOfWeek)
        assertTrue(reminder.isAfter(now))
    }
}
