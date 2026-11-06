# Sunday 1 November 2026 — Navigation and timetable model

## Objective
Build the minimal screen flow and testable timetable model/rules, after lecturer approval.

## Owners
- Cheela leads navigation among Today, Weekly, Add/Edit, and Settings; Bright reviews back behavior.
- Frank leads model/validation tests; Tehilla reviews the data contract; Mwenya checks the reminder boundary.

## Checklist — status checked 6 November 2026, implemented in canonical UniTime project
- [x] Implement `ClassEntry` model with stable ID, title, weekday, times, and venue in `app/src/main/kotlin/com/vibecoders/unitime/ClassEntry.kt`.
- [x] Implement and unit-test title/time validation and overlap rules.
- [x] Add Today, Weekly, Add/Edit, and Settings screens with bottom navigation.
- [x] Instrumented navigation test reaches Weekly from Today and passes on Pixel 10 Pro XL API 37 emulator.
- [x] Show empty state; form uses saveable fields to retain typed state through activity recreation; a user-journey test verifies a saved Room entry is still visible after recreation.
- [x] Use test-first implementation for validation/navigation/user-journey slices; currently local Git repository is initialized.
- [x] `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:connectedDebugAndroidTest` passes from canonical `/Users/tehillachiwama/projects/UniTime/`.

## Evidence and gate
Verified model/validation, overlaps, four screens, install/launch, add-class validation journey, Room activity-recreation journey, and Android navigation/database tests. Manual full navigation/back walk-through remains to do. Shared GitHub remote and all-five commits are not set up yet.