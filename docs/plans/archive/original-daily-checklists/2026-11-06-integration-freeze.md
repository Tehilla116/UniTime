# Friday 6 November 2026 — Integration and feature freeze

## Objective
Run the whole app against the rubric, fix acceptance blockers, and freeze features.

## Checklist
- [ ] Verify navigation/back across four screens, add/edit/delete, validation messages, sorting, scrolling, empty state, and overlap warning.
- [ ] Verify Room data survives rotation and app close/reopen.
- [ ] Verify notification opt-in and denial without crash or data loss.
- [ ] Check UI, model, validation, repository, Room, and notification responsibilities are separated.
- [ ] Run `./gradlew test` and `./gradlew assembleDebug` and keep actual outputs.
- [ ] All five take turns demonstrating and explaining a slice outside their main assignment.
- [ ] Freeze scope after all acceptance criteria pass; only fix blockers from now on.

## Pair review
Tehilla ↔ Frank (Room/form), Frank ↔ Bright (validation/list), Bright ↔ Cheela (navigation), Cheela ↔ Mwenya (Settings/notifications), Mwenya ↔ Tehilla (notification/data boundary).

## Evidence and gate
Evidence: completed rubric checklist, test/build logs, demo scenario, defects list. **GO** to docs/demo when core app is usable and all required behaviors are verified; otherwise cut optional polish.