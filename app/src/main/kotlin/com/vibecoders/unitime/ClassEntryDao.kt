package com.vibecoders.unitime

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassEntryDao {
    @Query("SELECT * FROM class_entries ORDER BY weekday, startMinute, title")
    fun observeAll(): Flow<List<ClassEntryEntity>>

    @Query("SELECT * FROM class_entries ORDER BY weekday, startMinute, title")
    suspend fun getAll(): List<ClassEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: ClassEntryEntity)

    @Query("DELETE FROM class_entries WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM class_entries")
    suspend fun deleteAll()
}
