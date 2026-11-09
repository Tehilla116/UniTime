# UniTime

## The problem

University and college students in Zambia may need to consult separate paper timetables and class-group messages to confirm weekly classes or changes to a class time or room. This can make it harder to check what class is next and where it will be held. UniTime keeps each student's recurring weekly classes and venues in one Android app, available offline. The timetable-fragmentation problem is a hypothesis to validate with students at more than one campus; it is not presented as completed user research.

## Group members

| Full Name | Student Number |
|-----------|----------------|
| Tehilla Chiwama | 2410014 |
| Bright Mwansa | 2410010 |
| Mwenya Simpungwe | 2410013 |
| Cheela Mulilo | 2410032 |
| Frank Mwelwa | 2410434 |

## How to build and run

1. Clone the group repository:

   ```bash
   git clone https://github.com/Tehilla116/UniTime.git
   ```

2. Open the cloned `UniTime` folder in Android Studio and wait for Gradle sync to finish. Install/accept the Android SDK platform and build tools required by the project (compile SDK 37).
3. Start an Android emulator or connect an Android phone (minimum SDK 24). Select the `app` run configuration and press **Run**.
4. To build a debug APK from the repository root, run:

   ```bash
   ./gradlew :app:assembleDebug
   ```

   Run local unit tests with `./gradlew :app:testDebugUnitTest`. With an emulator/device running, run connected Android tests with `./gradlew :app:connectedDebugAndroidTest`.

## Screens

1. **Today** — Shows the date and today's classes in start-time order, with an empty state when no classes are scheduled.
2. **Weekly Timetable** — Shows saved classes grouped by weekday and start time, and flags possible time clashes.
3. **Add/Edit Class** — Lets a student enter a class name, weekday, start/end times, and optional venue; it validates entries and supports editing and deleting.
4. **Settings** — Lets a student turn class reminders on or off and choose a 5-, 15-, or 30-minute lead time; the timetable remains usable if notification permission is denied.

## Research Note 1 — Room local database

**Owner:** Tehilla Chiwama.

**What it is.** Room is an Android Jetpack persistence library that provides an abstraction over SQLite for structured, on-device data.[1] An entity represents a database table and its rows.[2] A DAO (data access object) defines typed queries and insert, update, and delete operations.[3] A database class is the access point to stored data. Room checks declared SQL queries at compile time and cuts down on repetitive SQLite boilerplate.[1]

**Why UniTime needs it.** A student's timetable should remain available offline and after the app closes. UniTime stores each class's ID, name, weekday, start and end minutes, and optional venue in a local Room database. `ClassEntryEntity` represents the row, `ClassEntryDao` observes and changes entries, `ClassEntryRepository` maps database entities to timetable models, and `TimetableViewModel` exposes that data to the Compose screens. This keeps persistence logic separate from screen code and means the Today and Weekly screens can observe the same saved schedule.

**Where we learned it.** We started with the official Android Developers [Room guide][1], then referred to the official guides on [Room entities][2] and [Room DAOs][3], including their examples. We applied those concepts in `app/src/main/kotlin/com/vibecoders/unitime/ClassEntryEntity.kt`, `ClassEntryDao.kt`, `UniTimeDatabase.kt`, and `ClassEntryRepository.kt`; the persistence checks are in `app/src/androidTest/kotlin/com/vibecoders/unitime/UniTimeDatabaseTest.kt`.

**What went wrong and what we learned.** An early lifecycle check recreated the Activity and confirmed that a saved class was still visible, but Activity recreation alone does not prove that data survived closing and reopening the database. We added a file-backed Room test that inserts a class, closes the database, opens it again, and verifies the row. The database close/reopen test exists; a full app force-stop-and-relaunch walkthrough through the UI was still listed as outstanding in the project notes. This taught us to distinguish a passing screen/lifecycle test from a persistence test and to describe exactly what we verified rather than overstate it. We should run and record the app-level force-stop/relaunch check before claiming that entire flow has been demonstrated.

## Research Note 2 — Android notifications and background work

**Owner:** Mwenya Simpungwe.

**What it is.** Android notification channels group notifications so people can manage their behavior in system settings.[5] Android 13 and later also require an app to request the `POST_NOTIFICATIONS` runtime permission.[6] UniTime uses WorkManager for persistent background work: it is appropriate for tasks that should continue after the app leaves the foreground, but scheduled execution can be deferred by Android.[7]

**Why UniTime needs it.** Optional reminders can help a student notice an upcoming saved class without keeping the app open. In UniTime, Settings provides 5-, 15-, and 30-minute lead-time choices. `ClassReminderScheduler` schedules replaceable work for each class; `ClassReminderWorker` checks notification permission and reads the current class from Room before attempting to post a notification.[5][6] Editing a class replaces its scheduled reminder, while deleting it or switching reminders off cancels its work. Denying notification permission should not prevent normal timetable use.

**Where we learned it.** We studied official Android Developers guidance on [creating notifications][4] and [notification channels][5]. We also used the official guidance on [notification runtime permission][6] and [persistent background work with WorkManager][7]. We mapped those concepts to `ClassReminderScheduler.kt`, `ClassReminderWorker` in the same file, and the Settings UI in `UniTimeApp.kt`.

**What went wrong and what we learned.** During code review for this note, we found a likely due-time bug not covered by the existing scheduler/cancellation tests. When the worker runs at the reminder time, it recalculates the next reminder; the time helper treats a reminder due now as already passed and advances it by a week. The worker can therefore queue next week's reminder instead of posting this week's one. This needs a focused worker test and a fix, followed by an emulator check of permission granted, permission denied, and actual notification delivery. Until those checks are complete, we should not claim the reminder workflow is fully verified. Separately, even after a fix, WorkManager may delay execution, so exact-minute delivery cannot be promised.[7]

## Sources

[1]: https://developer.android.com/training/data-storage/room "Android Developers — Save data in a local database using Room"
[2]: https://developer.android.com/training/data-storage/room/defining-data "Android Developers — Define data using Room entities"
[3]: https://developer.android.com/training/data-storage/room/accessing-data "Android Developers — Accessing data using Room DAOs"
[4]: https://developer.android.com/develop/ui/compose/notifications/create-notification "Android Developers — Create a basic notification"
[5]: https://developer.android.com/develop/ui/compose/notifications/channels "Android Developers — Create and manage notification channels"
[6]: https://developer.android.com/develop/ui/compose/notifications/notification-permission "Android Developers — Notification runtime permission"
[7]: https://developer.android.com/develop/background-work/background-tasks/persistent "Android Developers — Task scheduling with WorkManager"

## Project details

The app is written in Kotlin with Jetpack Compose. Its application ID is `com.vibecoders.unitime`, and its minimum SDK is 24. See [`Proposal.md`](Proposal.md) for fuller project requirements.
