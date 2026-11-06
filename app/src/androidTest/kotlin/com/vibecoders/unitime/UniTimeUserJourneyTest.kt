package com.vibecoders.unitime

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UniTimeUserJourneyTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun studentCanAddClassAndSeeItInWeekAfterActivityRecreation() {
        val uniqueTitle = "Chemistry ${System.currentTimeMillis()}"
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
    fun blankClassNameShowsValidationMessageAndDoesNotLeaveForm() {
        composeRule.onNodeWithText("Add class", useUnmergedTree = true).performClick()
        composeRule.onNodeWithText("Save", useUnmergedTree = true).performClick()

        composeRule.onNodeWithText("Enter a class name.").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithText("Add class", useUnmergedTree = true).fetchSemanticsNodes().size >= 2)
    }
}
