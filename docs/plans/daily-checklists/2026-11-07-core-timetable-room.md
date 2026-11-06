# Saturday 7 November 2026 — Core timetable and Room

## Objective
Deliver the smallest useful timetable workflow and persistent local data.

## Owners
- Frank: Add/Edit form, validation errors, add/edit/delete tests.
- Bright: Today/Week data lists, sort, scroll, empty state, overlap warning.
- Cheela: navigation/back integration.
- Tehilla: Room entity/DAO/database/repository and persistence test.
- Mwenya: notification integration spike only after data contract is stable.

## Checklist
- [ ] Implement only approved `ClassEntry` fields and core workflow.
- [ ] Test blank name, missing/invalid time, end <= start, valid entry, CRUD, ordering and overlap cases.
- [ ] Backed list uses same state/repository, not hard-coded samples.
- [ ] Room-backed class survives app close/reopen; verify device rotation too.
- [ ] For each slice test-first → observed RED → minimum implementation → GREEN → regression → own-account commit.
- [ ] Run `./gradlew test` and `./gradlew assembleDebug` from the real Android project root and record actual outputs.

## Gate/evidence
A running app has at least three usable screens, valid/invalid form behavior, data-backed timetable, and Room close/reopen proof. If Room blocks, timebox troubleshooting and tell the team; do not claim Compose-only state is durable storage.