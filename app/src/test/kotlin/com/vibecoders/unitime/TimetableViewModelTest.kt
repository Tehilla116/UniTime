package com.vibecoders.unitime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TimetableViewModelTest {
    @Test
    fun repositorySaveAddsAndReplacesSameIdEntry() = runBlocking {
        val dao = FakeClassEntryDao()
        val repository = ClassEntryRepository(dao)
        val original = ClassEntry(id = "c1", title = "Math", weekday = Weekday.MONDAY, startMinute = 600, endMinute = 660)
        repository.save(original)
        repository.save(original.copy(title = "Math II"))

        assertEquals(1, dao.getAll().size)
        assertEquals("Math II", dao.getAll().single().title)
    }

    @Test
    fun repositoryDeleteRemovesEntry() = runBlocking {
        val dao = FakeClassEntryDao()
        val repository = ClassEntryRepository(dao)
        repository.save(ClassEntry(id = "c1", title = "Math", weekday = Weekday.MONDAY, startMinute = 600, endMinute = 660))
        repository.delete("c1")

        assertTrue(dao.getAll().isEmpty())
    }

    @Test
    fun repositoryListsEntriesInWeekOrder() = runBlocking {
        val dao = FakeClassEntryDao()
        val repository = ClassEntryRepository(dao)
        repository.save(ClassEntry(id = "f", title = "Physics", weekday = Weekday.FRIDAY, startMinute = 540, endMinute = 600))
        repository.save(ClassEntry(id = "m", title = "Math", weekday = Weekday.MONDAY, startMinute = 600, endMinute = 660))

        assertEquals(listOf("m", "f"), repository.entries.first().map { it.id })
    }
}

private class FakeClassEntryDao : ClassEntryDao {
    private val data = linkedMapOf<String, ClassEntryEntity>()
    private val stream = MutableStateFlow<List<ClassEntryEntity>>(emptyList())

    override fun observeAll(): Flow<List<ClassEntryEntity>> = stream
    override suspend fun getClassEntry(entryId: String) = data[entryId]
    override suspend fun getAll() = data.values.toList()
    override suspend fun upsert(entry: ClassEntryEntity) {
        data[entry.id] = entry
        stream.value = data.values.toList()
    }
    override suspend fun deleteById(id: String) {
        data.remove(id)
        stream.value = data.values.toList()
    }
    override suspend fun deleteAll() {
        data.clear()
        stream.value = emptyList()
    }
}
