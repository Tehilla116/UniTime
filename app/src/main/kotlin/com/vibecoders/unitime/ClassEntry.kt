package com.vibecoders.unitime

enum class Weekday(val displayName: String) {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday")
}

data class ClassEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val weekday: Weekday,
    val startMinute: Int,
    val endMinute: Int,
    val venue: String = ""
)

enum class ClassEntryError {
    EMPTY_TITLE,
    INVALID_WEEKDAY,
    INVALID_START_TIME,
    INVALID_END_TIME,
    END_NOT_AFTER_START
}

data class ClassEntryValidation(
    val entry: ClassEntry?,
    val errors: Set<ClassEntryError>
) {
    val isValid: Boolean get() = entry != null && errors.isEmpty()
}

fun validateClassEntry(
    title: String,
    weekday: Weekday?,
    startMinute: Int,
    endMinute: Int,
    venue: String = "",
    id: String = java.util.UUID.randomUUID().toString()
): ClassEntryValidation {
    val errors = linkedSetOf<ClassEntryError>()
    val cleanTitle = title.trim()
    val cleanVenue = venue.trim()
    if (cleanTitle.isEmpty()) errors += ClassEntryError.EMPTY_TITLE
    if (weekday == null) errors += ClassEntryError.INVALID_WEEKDAY
    if (startMinute !in 0 until 24 * 60) errors += ClassEntryError.INVALID_START_TIME
    if (endMinute !in 0 until 24 * 60) errors += ClassEntryError.INVALID_END_TIME
    if (startMinute in 0 until 24 * 60 && endMinute in 0 until 24 * 60 && endMinute <= startMinute) {
        errors += ClassEntryError.END_NOT_AFTER_START
    }
    val entry = if (errors.isEmpty()) {
        ClassEntry(id, cleanTitle, requireNotNull(weekday), startMinute, endMinute, cleanVenue)
    } else null
    return ClassEntryValidation(entry, errors)
}

fun entriesOverlap(first: ClassEntry, second: ClassEntry): Boolean =
    first.weekday == second.weekday &&
        first.id != second.id &&
        first.startMinute < second.endMinute &&
        second.startMinute < first.endMinute

fun sortedWeek(entries: List<ClassEntry>): List<ClassEntry> = entries.sortedWith(
    compareBy<ClassEntry> { it.weekday.ordinal }.thenBy { it.startMinute }.thenBy { it.title.lowercase() }
)
