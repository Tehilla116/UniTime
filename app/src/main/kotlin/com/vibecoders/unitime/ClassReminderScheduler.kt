package com.vibecoders.unitime

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

private const val CLASS_REMINDER_CHANNEL = "class_reminders"
private const val REMINDER_WORK_PREFIX = "unitime-class-reminder-"
private const val REMINDER_TAG_PREFIX = "unitime-class-tag-"

class ClassReminderScheduler(private val context: Context) {
    fun schedule(entry: ClassEntry, leadMinutes: Int, now: ZonedDateTime = ZonedDateTime.now()) {
        val reminderAt = nextReminderTime(entry.weekday, entry.startMinute, leadMinutes, now)
        val delay = Duration.between(now, reminderAt).toMillis().coerceAtLeast(0L)
        val request = OneTimeWorkRequestBuilder<ClassReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(REMINDER_TAG_PREFIX + entry.id)
            .setInputData(
                androidx.work.workDataOf(
                    ClassReminderWorker.KEY_ID to entry.id,
                    ClassReminderWorker.KEY_TITLE to entry.title,
                    ClassReminderWorker.KEY_VENUE to entry.venue,
                    ClassReminderWorker.KEY_WEEKDAY to entry.weekday.name,
                    ClassReminderWorker.KEY_START_MINUTE to entry.startMinute,
                    ClassReminderWorker.KEY_LEAD_MINUTES to leadMinutes
                )
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            REMINDER_WORK_PREFIX + entry.id,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancel(entryId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(REMINDER_TAG_PREFIX + entryId)
        WorkManager.getInstance(context).cancelUniqueWork(REMINDER_WORK_PREFIX + entryId)
    }
}

class ClassReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val entryId = inputData.getString(KEY_ID) ?: return Result.failure()
        val title = inputData.getString(KEY_TITLE) ?: return Result.failure()
        val venue = inputData.getString(KEY_VENUE).orEmpty()
        val weekday = inputData.getString(KEY_WEEKDAY)?.let { runCatching { Weekday.valueOf(it) }.getOrNull() }
            ?: return Result.failure()
        val start = inputData.getInt(KEY_START_MINUTE, -1)
        val lead = inputData.getInt(KEY_LEAD_MINUTES, -1)
        if (start !in 0 until 24 * 60 || lead !in 0..180) return Result.failure()

        val database = Room.databaseBuilder(applicationContext, UniTimeDatabase::class.java, "unitime.db").build()
        val currentEntry = try {
            database.classEntryDao().getAll().firstOrNull { it.id == entryId }?.toModel()
        } finally {
            database.close()
        } ?: return Result.success()

        val nextReminder = runCatching {
            nextReminderTime(currentEntry.weekday, currentEntry.startMinute, lead, ZonedDateTime.now())
        }.getOrElse { return Result.failure() }
        if (nextReminder.isAfter(ZonedDateTime.now().plusMinutes(1))) {
            val nextRequest = OneTimeWorkRequestBuilder<ClassReminderWorker>()
                .setInitialDelay(Duration.between(ZonedDateTime.now(), nextReminder).toMillis().coerceAtLeast(0L), TimeUnit.MILLISECONDS)
                .addTag(REMINDER_TAG_PREFIX + entryId)
                .setInputData(inputData)
                .build()
            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                REMINDER_WORK_PREFIX + entryId,
                ExistingWorkPolicy.REPLACE,
                nextRequest
            )
            return Result.success()
        }

        val content = buildString {
            append("Starts at ")
            append("%02d:%02d".format(start / 60, start % 60))
            if (venue.isNotBlank()) append(" · ").append(venue)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            applicationContext.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return Result.success()
        createReminderChannel(applicationContext)
        val notification = NotificationCompat.Builder(applicationContext, CLASS_REMINDER_CHANNEL)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        return try {
            NotificationManagerCompat.from(applicationContext).notify(title.hashCode(), notification)
            Result.success()
        } catch (_: SecurityException) {
            Result.success()
        }
    }

    companion object {
        const val KEY_ID = "class_id"
        const val KEY_TITLE = "class_title"
        const val KEY_VENUE = "class_venue"
        const val KEY_WEEKDAY = "class_weekday"
        const val KEY_START_MINUTE = "class_start_minute"
        const val KEY_LEAD_MINUTES = "reminder_lead_minutes"
    }
}

private fun createReminderChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(
                CLASS_REMINDER_CHANNEL,
                "Class reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Optional reminders before saved classes" }
        )
    }
}
