package com.vibecoders.unitime

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZonedDateTime

@RunWith(AndroidJUnit4::class)
class ClassReminderIntegrationTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setTaskExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        context.getSharedPreferences("unitime_settings", Context.MODE_PRIVATE).edit()
            .putBoolean("reminders_enabled", false)
            .putInt("reminder_lead_minutes", 15)
            .commit()
        context.deleteDatabase("unitime.db")
    }

    @After
    fun tearDown() {
        WorkManager.getInstance(context).cancelAllWork()
        context.getSharedPreferences("unitime_settings", Context.MODE_PRIVATE).edit().clear().commit()
        context.deleteDatabase("unitime.db")
    }

    @Test
    fun classReminderSchedulingReplacesDuplicateWorkForSameClass() {
        val scheduler = ClassReminderScheduler(context)
        val entry = ClassEntry("replace-test", "Replacement class", Weekday.MONDAY, 9 * 60, 10 * 60)
        val now = ZonedDateTime.now().withHour(1).withMinute(0).withSecond(0).withNano(0)

        scheduler.schedule(entry, 15, now)
        scheduler.schedule(entry, 30, now)

        val requests = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork("unitime-class-reminder-${entry.id}").get()
        assertEquals(1, requests.count { !it.state.isFinished })

    }

    @Test
    fun cancellingClassReminderRemovesItsScheduledWork() {
        val scheduler = ClassReminderScheduler(context)
        val entry = ClassEntry("cancel-test", "Cancelled class", Weekday.MONDAY, 9 * 60, 10 * 60)
        scheduler.schedule(entry, 15, ZonedDateTime.now().withHour(1).withMinute(0).withSecond(0).withNano(0))

        scheduler.cancel(entry.id)

        val requests = WorkManager.getInstance(context)
            .getWorkInfosForUniqueWork("unitime-class-reminder-${entry.id}").get()
        assertTrue(requests.isEmpty() || requests.all { it.state.isFinished })
    }

    @Test
    fun repositoryKeepsClassDataUsableWhenNotificationsAreOff() = runBlocking {
        val database = Room.inMemoryDatabaseBuilder(context, UniTimeDatabase::class.java).build()
        try {
            val repository = ClassEntryRepository(database.classEntryDao())
            val entry = ClassEntry("denial-test", "No-notification class", Weekday.FRIDAY, 9 * 60, 10 * 60)
            repository.save(entry)
            assertEquals(entry, repository.entries.first().single())
        } finally {
            database.close()
        }
    }
}
