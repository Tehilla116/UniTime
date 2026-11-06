# Wednesday 4 November 2026 — Room persistence and lifecycle

## Objective
Persist the user's timetable in Room and prove it survives rotation and app close/reopen.

## Owners
Tehilla leads Room and owns Research Topic 1; Frank reviews field mapping; Bright verifies lists consume stored data; Cheela checks navigation/lifecycle; Mwenya assists compatibility and integration.

## Checklist
- [ ] Check official Android Room guide and Kotlin samples against project Kotlin/AGP versions before choosing compatible Room/KSP artifacts.
- [ ] Implement entity, DAO, database, and a repository boundary separate from Compose screens.
- [ ] Test insert, list, update, and delete; add a real database persistence test.
- [ ] On emulator/device add a class, rotate, close/reopen app, and verify the class is still visible.
- [ ] Draft Tehilla's Research Note: concept, UniTime use, official source/sample, what broke and fix.
- [ ] Run `./gradlew test` and `./gradlew assembleDebug`; record actual outcomes.

## Evidence and gate
Evidence: schema/model, passing persistence tests, close/reopen demonstration, research note. **GO** only after actual persisted data survives app restart; UI-only in-memory state is not enough.