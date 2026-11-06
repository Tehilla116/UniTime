package com.vibecoders.unitime

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UniTimeNavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun todayAndWeeklyScreensAreReachable() {
        assertTrue(composeRule.onAllNodesWithText("Today").fetchSemanticsNodes().isNotEmpty())
        composeRule.onNodeWithText("Weekly timetable").assertDoesNotExist()
        composeRule.onNodeWithText("Week").performClick()
        composeRule.onNodeWithText("Weekly timetable").assertIsDisplayed()
    }
}
