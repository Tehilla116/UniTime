package com.vibecoders.unitime

import android.app.Application
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    private val classReminders = ClassReminderScheduler(application)
    private val database = Room.databaseBuilder(
        application,
        UniTimeDatabase::class.java,
        "unitime.db"
    ).build()
    private val repository = ClassEntryRepository(database.classEntryDao())

    val entries: StateFlow<List<ClassEntry>> = repository.entries.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun save(entry: ClassEntry) {
        viewModelScope.launch { repository.save(entry) }
        val app = getApplication<Application>()
        val preferences = app.getSharedPreferences("unitime_settings", Context.MODE_PRIVATE)
        val leadMinutes = preferences.getInt("reminder_lead_minutes", 15)
            .takeIf { it == 5 || it == 15 || it == 30 } ?: 15
        if (preferences.getBoolean("reminders_enabled", false) && NotificationManagerCompat.from(app).areNotificationsEnabled()) {
            classReminders.schedule(entry, leadMinutes)
        } else {
            classReminders.cancel(entry.id)
        }
    }

    fun scheduleReminder(entry: ClassEntry, leadMinutes: Int) {
        if (NotificationManagerCompat.from(getApplication()).areNotificationsEnabled()) classReminders.schedule(entry, leadMinutes)
        else classReminders.cancel(entry.id)
    }

    fun cancelReminder(entryId: String) {
        classReminders.cancel(entryId)
    }

    fun delete(entryId: String) {
        viewModelScope.launch { repository.delete(entryId) }
        classReminders.cancel(entryId)
    }

    override fun onCleared() {
        database.close()
        super.onCleared()
    }
}
