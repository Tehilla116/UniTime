package com.vibecoders.unitime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClassValidationTest {
    @Test
    fun blankTitleIsRejected() {
        val result = validateClassEntry(
            title = "  ", weekday = Weekday.MONDAY,
            startMinute = 9 * 60, endMinute = 10 * 60, venue = "A1"
        )

        assertTrue(result.errors.contains(ClassEntryError.EMPTY_TITLE))
    }

    @Test
    fun endMustBeAfterStart() {
        val result = validateClassEntry(
            title = "Mobile Development", weekday = Weekday.MONDAY,
            startMinute = 10 * 60, endMinute = 10 * 60, venue = "A1"
        )

        assertTrue(result.errors.contains(ClassEntryError.END_NOT_AFTER_START))
    }

    @Test
    fun validEntryTrimsTitleAndVenue() {
        val result = validateClassEntry(
            title = "  Mobile Development  ", weekday = Weekday.MONDAY,
            startMinute = 9 * 60, endMinute = 10 * 60, venue = "  A1  "
        )

        assertTrue(result.isValid)
        assertEquals("Mobile Development", result.entry?.title)
        assertEquals("A1", result.entry?.venue)
    }
}
