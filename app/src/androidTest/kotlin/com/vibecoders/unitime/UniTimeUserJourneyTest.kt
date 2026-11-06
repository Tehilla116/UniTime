package com.vibecoders.unitime

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UniTimeUserJourneyTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun studentCanAddClassAndSeeItInWeekAfterActivityRecreation() {
        val uniqueTitle = "Chemistry ${System.currentTimeMillis()}"
        val preferences = composeRule.activity.getSharedPreferences("unitime_settings", android.content.Context.MODE_PRIVATE)
        preferences.edit().putBoolean("reminders_enabled", false).apply()
        val requestWork = androidx.work.WorkManager.getInstance(composeRule.activity)
        requestWork.cancelAllWork()
        composeRule.onNodeWithText("Add class", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Class name").performTextInput(uniqueTitle)
        composeRule.onNodeWithText("Save", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Weekly timetable").assertIsDisplayed()
        composeRule.onNodeWithText(uniqueTitle).assertIsDisplayed()

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()
        composeRule.onNodeWithText(uniqueTitle).assertIsDisplayed()
    }

    @Test
    fun weekdayCanBeSelectedFromAListInsteadOfTyped() {
        val uniqueTitle = "Dropdown ${System.currentTimeMillis()}"
        composeRule.onNodeWithText("Add class", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Class name").performTextInput(uniqueTitle)
        composeRule.onNodeWithText("Weekday (Monday–Sunday): Monday").performClick()
        composeRule.onNodeWithText("Friday").performClick()
        composeRule.onNodeWithText("Save", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Weekly timetable").assertIsDisplayed()
        composeRule.onNodeWithText("Friday").assertIsDisplayed()
        composeRule.onNodeWithText(uniqueTitle).assertIsDisplayed()
    }

    @Test
    fun classTimesCanBePickedFromScrollableHourAndMinuteLists() {
        val uniqueTitle = "Scrollable Time ${System.currentTimeMillis()}"
        composeRule.onNodeWithText("Add class", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Class name").performTextInput(uniqueTitle)
        composeRule.onNodeWithText("Weekday (Monday–Sunday): Monday").performClick()
        composeRule.onNodeWithText("Friday").performClick()

        composeRule.onNodeWithText("Start time: 09:00").performClick()
        composeRule.onNodeWithTag("hour-options").performScrollToNode(hasTestTag("hour-option-22"))
        composeRule.onNodeWithTag("hour-option-22").performClick()
        composeRule.onNodeWithTag("minute-options").performScrollToNode(hasTestTag("minute-option-35"))
        composeRule.onNodeWithTag("minute-option-35").performClick()
        composeRule.onNodeWithText("Set time").performClick()

        composeRule.onNodeWithText("End time: 10:00").performClick()
        composeRule.onNodeWithTag("hour-options").performScrollToNode(hasTestTag("hour-option-22"))
        composeRule.onNodeWithTag("hour-option-22").performClick()
        composeRule.onNodeWithTag("minute-options").performScrollToNode(hasTestTag("minute-option-40"))
        composeRule.onNodeWithTag("minute-option-40").performClick()
        composeRule.onNodeWithText("Set time").performClick()

        composeRule.onNodeWithText("Save", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Weekly timetable").assertIsDisplayed()
        composeRule.onNodeWithText("22:35–22:40").assertIsDisplayed()
        composeRule.onNodeWithText(uniqueTitle).assertIsDisplayed()
    }

    @Test
    fun blankClassNameShowsValidationMessageAndDoesNotLeaveForm() {
        composeRule.onNodeWithText("Add class", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Save", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("Enter a class name.").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithText("Add class", useUnmergedTree = true).fetchSemanticsNodes().size >= 2)
    }
}
