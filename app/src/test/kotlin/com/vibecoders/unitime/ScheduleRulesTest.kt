package com.vibecoders.unitime

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScheduleRulesTest {
    private fun entry(day: Weekday, start: Int, end: Int, title: String = "Class") =
        ClassEntry(title = title, weekday = day, startMinute = start, endMinute = end)

    @Test
    fun overlappingEntriesOnSameDayConflict() {
        assertTrue(entriesOverlap(entry(Weekday.MONDAY, 9 * 60, 10 * 60), entry(Weekday.MONDAY, 9 * 60 + 30, 11 * 60)))
    }

    @Test
    fun adjacentEntriesDoNotConflict() {
        assertFalse(entriesOverlap(entry(Weekday.MONDAY, 9 * 60, 10 * 60), entry(Weekday.MONDAY, 10 * 60, 11 * 60)))
    }

    @Test
    fun entriesOnDifferentDaysDoNotConflict() {
        assertFalse(entriesOverlap(entry(Weekday.MONDAY, 9 * 60, 10 * 60), entry(Weekday.TUESDAY, 9 * 60, 10 * 60)))
    }
}
