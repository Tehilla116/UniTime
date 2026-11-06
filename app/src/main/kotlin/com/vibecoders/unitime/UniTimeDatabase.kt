package com.vibecoders.unitime

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ClassEntryEntity::class], version = 1, exportSchema = false)
abstract class UniTimeDatabase : RoomDatabase() {
    abstract fun classEntryDao(): ClassEntryDao
}
