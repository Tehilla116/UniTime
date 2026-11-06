package com.vibecoders.unitime

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimetableViewModel(application: Application) : AndroidViewModel(application) {
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
    }

    fun scheduleReminder(entry: ClassEntry, leadMinutes: Int) {
        ClassReminderScheduler(getApplication()).schedule(entry, leadMinutes)
    }

    fun cancelReminder(entryId: String) {
        ClassReminderScheduler(getApplication()).cancel(entryId)
    }

    fun delete(entryId: String) {
        viewModelScope.launch { repository.delete(entryId) }
        cancelReminder(entryId)
    }

    override fun onCleared() {
        database.close()
        super.onCleared()
    }
}
