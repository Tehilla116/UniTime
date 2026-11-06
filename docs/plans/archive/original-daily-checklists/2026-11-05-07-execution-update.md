# Day 5 — Thursday 5 November 2026: Optional class reminders

## Goal
Research and implement the second concept required by the brief: optional class reminders using Android notification permission/channel APIs and WorkManager. Reminders must not block the timetable if permission is denied. WorkManager can defer execution; do not claim exact-minute delivery.[1][2][3]

## Implementation verified in the repository
- [x] Declared `POST_NOTIFICATIONS` in `app/src/main/AndroidManifest.xml`.
- [x] Settings offers opt-in reminders and 5/15/30 minute lead times; permission is requested only after the user turns reminders on.
- [x] Denying permission leaves the timetable usable; the worker exits without posting when permission is missing.
- [x] Added a notification channel and unique, replaceable WorkManager work per class.
- [x] Saving/editing reschedules; deleting or turning reminders off cancels pending work; lead-time changes replace scheduled work.
- [x] Worker reads the latest class from Room before acting and skips a class that has been deleted.
- [x] Added reminder-time unit tests, including week rollover and unsupported lead-time cases.
- [ ] Still required: run on emulator/device and observe the notification; exercise permission grant/deny from Settings and confirm editing/deleting changes queued work. Source/build checks are not a substitute for this runtime evidence.

## Research Topic 2 — Mwenya Simpungwe
**Concept:** Android notification channels, runtime `POST_NOTIFICATIONS` permission, and WorkManager background work.

**Why UniTime needs it:** A student can opt in to a reminder before a saved weekly class without requiring the app to remain open. Permission denial must not affect class creation, editing, or timetable viewing.

**Sources studied:** Android Developers, [Notification runtime permission](https://developer.android.com/develop/ui/compose/notifications/notification-permission), [Create and manage notification channels](https://developer.android.com/develop/ui/compose/notifications/channels), and [Task scheduling with WorkManager](https://developer.android.com/develop/background-work/background-tasks/persistent).

**Implementation mapping:** `POST_NOTIFICATIONS` is declared in the manifest; Compose requests it from the Settings switch; `ClassReminderScheduler` enqueues unique one-time work; `ClassReminderWorker` checks permission, reads the current saved class, creates the `class_reminders` channel, and posts a notification.

**Limitations / issues to explain:** WorkManager is persistent and suitable for deferrable background work, but the operating system controls its execution time, so a reminder is not guaranteed to arrive at the exact selected minute.[3] The user may also disable notifications or the channel in Android settings. Runtime delivery and the denial flow are not yet evidenced by an emulator walkthrough.

---

# Day 6 — Friday 6 November 2026: Integration and release gate

## Goal
Run the complete Android checks, fix verified blockers, inspect repository/README state, and avoid claiming unverified delivery work.

## Checklist
- [x] Re-ran `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:connectedDebugAndroidTest`; all tasks passed on the Pixel 10 Pro XL API 37 emulator.
- [x] Verified connected tests cover navigation, invalid blank-name feedback, add-and-display, Activity recreation, Room CRUD and Room close/reopen persistence.
- [x] Confirmed the repo's configured shared remote is `https://github.com/Tehilla116/UniTime.git`, branch `main` matches `origin/main`, and `local.properties`/build outputs are ignored.
- [ ] Full OS process force-stop/relaunch through the UI not yet demonstrated.
- [ ] Instrumented UI edit/delete, malformed-time, back navigation, scroll/empty-state/clash journeys not yet specifically covered by connected tests.
- [ ] Notification grant/deny and actual posted notification not yet run through on the emulator.
- [ ] Shared remote currently has one commit; no evidence of contributions from the other four accounts.
- [ ] `README.md`, both research notes, student numbers, clean-clone rehearsal, demo and peer ratings are not yet present/verified.

## Gate
Do not call the app release-ready until every unchecked acceptance item needed for the brief has actual evidence. Use the active Day 7–9 checklists for remaining deliverables.

---

# Day 7 — Saturday 7 November 2026: README, research notes, and clone check

## Goal
Make another team member able to understand, clone, build and run the app using repository instructions, and include the required two concept research notes.

## Required work
- [ ] Create root `README.md` stating problem, target users, screen list, data behavior, build/run instructions, limitations and demo guidance.
- [ ] Tehilla Chiwama completes the Room note, Frank Mwelwa reviews it.
- [ ] Mwenya Simpungwe completes the notification/background-work note, Bright Mwansa reviews it.
- [ ] Explain the concepts and map them to actual code; distinguish implemented code from behavior not yet tested on-device.
- [ ] Record actual trouble encountered during research/implementation (owners should supply their own experience; do not invent a team member's personal account).
- [ ] Have a teammate follow the README from a fresh clone and record the result.
- [ ] Confirm student numbers and meaningful commits from all five GitHub accounts; do not infer this from the repository owner alone.
- [ ] Run unit tests, debug assemble, and connected tests again after documentation or code changes.

## Current evidence before Day 7 work
- `origin/main` is configured and reachable, but the remote branch contains one commit only.
- No root README exists in the repo snapshot inspected on 6 November.
- Local SDK path is private machine config in ignored `local.properties`; a clean clone needs Android Studio/Android SDK setup described in README, not that file committed.
- Official Room documentation describes Room as an abstraction over SQLite with entities, DAOs and a database. UniTime uses this structure for offline timetable entries.[4]

## Gate
Do not check any deliverable complete until the file exists and a fresh-clone build/run has been performed. Demo, peer ratings and submission remain tracked in the active 8–9 November checklists.

## Sources
[1] Android Developers, Notification runtime permission — https://developer.android.com/develop/ui/compose/notifications/notification-permission
[2] Android Developers, Create and manage notification channels — https://developer.android.com/develop/ui/compose/notifications/channels
[3] Android Developers, Task scheduling with WorkManager — https://developer.android.com/develop/background-work/background-tasks/persistent
[4] Android Developers, Save data in a local database using Room — https://developer.android.com/training/data-storage/room
