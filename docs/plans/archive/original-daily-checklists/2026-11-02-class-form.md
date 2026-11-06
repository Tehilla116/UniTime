# Monday 2 November 2026 — Add/Edit Class form

## Objective
Complete class entry, edit/delete actions, and clear form validation.

## Owners
Frank leads the form and validation; Tehilla reviews data/repository boundaries; Bright checks list refresh; Cheela verifies return navigation; Mwenya checks reminder options do not become required fields.

## Checklist — status checked 6 November 2026, implemented in canonical UniTime project
- [x] Add/Edit form fields: class name, weekday, start time, end time, optional venue.
- [x] Unit tests cover blank title, end <= start, and valid trimmed entry.
- [x] Inline user-facing validation errors are shown and invalid entries are not saved; Android user-journey validation test passes.
- [x] Add class is tested end-to-end and appears in Weekly; saved Room-backed entry remains visible after Activity recreation.
- [x] Form fields use `rememberSaveable` keyed to class ID so typed state is retained through activity recreation.
- [x] `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:connectedDebugAndroidTest` passes in canonical root.
- [ ] Automated UI test for editing and deleting an existing class remains to do; implementation buttons/behavior are present but not yet verified by an instrumented CRUD journey.

## Evidence and gate
Validated add-class, blank-name error, time validation, and activity-recreation persistence. Add/edit/delete code is present; edit/delete user paths have not yet been proven in emulator tests. GitHub commits by each member remain outstanding.