package com.vibecoders.unitime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClassEntryRepository(private val dao: ClassEntryDao) {
    val entries: Flow<List<ClassEntry>> = dao.observeAll().map { entities ->
        entities.map(ClassEntryEntity::toModel).sortedWith(
            compareBy<ClassEntry> { it.weekday.ordinal }.thenBy { it.startMinute }.thenBy { it.title.lowercase() }
        )
    }

    suspend fun save(entry: ClassEntry) = dao.upsert(entry.toEntity())

    suspend fun delete(id: String) = dao.deleteById(id)
}
