# UniTime

UniTime is an Android timetable app project for university and college students. Its proposed first version is intended to help students manage a personal weekly class schedule, see today's classes, review possible time clashes, and keep timetable entries available offline. Optional class reminders are a later enhancement; the timetable should remain usable if notifications are not enabled.

> **Project status:** The current Android app is an early Jetpack Compose scaffold. The launcher currently opens a profile screen, and the Home and Timetable screens are placeholders. The features described above are the proposed scope, not a claim that they are already implemented.

## Project details

- **Platform:** Android
- **Language/UI:** Kotlin and Jetpack Compose
- **Application ID:** `com.zut.campusconnectmvp`
- **Minimum Android SDK:** 24
- **Main module:** `app`

## Build and run

Open the project in Android Studio, allow Gradle sync to finish, then run the `app` configuration on an emulator or connected Android device.

To build a debug APK from the project root:

```bash
./gradlew :app:assembleDebug
```

## Proposed first-version scope

- Add, edit, and remove a class with its name, weekday, start/end time, and optional venue.
- View today's classes in time order and browse the weekly timetable.
- Warn about overlapping classes on the same day.
- Save timetable entries locally for offline use and persistence after closing the app.
- Consider opt-in class reminders after the core timetable features work.

## Group members

- [ ] Record and verify the full names and student numbers of all five members exactly as printed on their student ID cards.

The names below are copied from the project proposal and must be checked against each member's ID card. Student numbers were not available when this README was created; fill them in exactly as printed before submission.

| # | Full name (exactly as on student ID card) | Student number (exactly as on student ID card) |
|---:|---|---|
| 1 | Tehilla Chiwama *(verify against ID)* | **[Enter from student ID card]** |
| 2 | Frank Mwelwa *(verify against ID)* | **[Enter from student ID card]** |
| 3 | Bright Mwansa *(verify against ID)* | **[Enter from student ID card]** |
| 4 | Cheela Mulilo *(verify against ID)* | **[Enter from student ID card]** |
| 5 | Mwenya Simpungwe *(verify against ID)* | **[Enter from student ID card]** |

## Research topics

1. **Room local database:** entities, DAOs, and queries for offline timetable storage.
2. **Android notifications:** notification channels and runtime permission for optional reminders.

See [`Proposal.md`](Proposal.md) for the project proposal and detailed feature requirements.
