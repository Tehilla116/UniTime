package com.vibecoders.unitime

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class_entries")
data class ClassEntryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val weekday: String,
    val startMinute: Int,
    val endMinute: Int,
    val venue: String
)

fun ClassEntry.toEntity() = ClassEntryEntity(id, title, weekday.name, startMinute, endMinute, venue)

fun ClassEntryEntity.toModel() = ClassEntry(
    id = id,
    title = title,
    weekday = Weekday.valueOf(weekday),
    startMinute = startMinute,
    endMinute = endMinute,
    venue = venue
)
