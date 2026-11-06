package com.vibecoders.unitime

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UniTimeDatabaseTest {
    private lateinit var database: UniTimeDatabase
    private lateinit var dao: ClassEntryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, UniTimeDatabase::class.java).build()
        dao = database.classEntryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun entryCanBeInsertedReadUpdatedAndDeleted() = runBlocking {
        val first = ClassEntryEntity("id-1", "Math", Weekday.MONDAY.name, 600, 660, "A1")
        dao.upsert(first)
        assertEquals(first, dao.getAll().single())

        val changed = first.copy(title = "Math II")
        dao.upsert(changed)
        assertEquals("Math II", dao.getAll().single().title)

        dao.deleteById(first.id)
        assertEquals(0, dao.getAll().size)
    }

    @Test
    fun classEntrySurvivesDatabaseCloseAndReopen() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "room-reopen-test.db"
        context.deleteDatabase(databaseName)

        val firstDatabase = Room.databaseBuilder(context, UniTimeDatabase::class.java, databaseName).build()
        val expected = ClassEntryEntity("persistent-1", "Biology", Weekday.TUESDAY.name, 540, 600, "Lab 2")
        firstDatabase.classEntryDao().upsert(expected)
        firstDatabase.close()

        val reopenedDatabase = Room.databaseBuilder(context, UniTimeDatabase::class.java, databaseName).build()
        try {
            assertEquals(expected, reopenedDatabase.classEntryDao().getClassEntry(expected.id))
        } finally {
            reopenedDatabase.close()
            context.deleteDatabase(databaseName)
        }
    }
}
