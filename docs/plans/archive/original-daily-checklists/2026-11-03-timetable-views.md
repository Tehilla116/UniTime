# Tuesday 3 November 2026 — Today and weekly timetable

## Objective
Show the same saved entries on Today and Weekly Timetable, with sorting, scrolling, empty states, and overlap warnings.

## Owners
Bright leads both lists and overlap display; Cheela pairs on screen navigation; Frank verifies form handoff; Tehilla verifies shared data source; Mwenya checks reminder display context.

## Checklist
- [ ] Today shows the selected current weekday's classes ordered by start time.
- [ ] Weekly view groups entries by weekday and scrolls for long lists.
- [ ] Empty schedule has explanatory text and an Add class action.
- [ ] Overlap rule: same weekday and intersecting time intervals; flag a clash without silently changing or rejecting entries.
- [ ] Tests cover ordering, different weekdays, adjacent non-overlap, and actual overlap.
- [ ] Add/edit from either list returns with changes reflected in both.
- [ ] Run focused tests, `./gradlew test`, and `./gradlew assembleDebug`; commit meaningful slices.

## Evidence and gate
Evidence: test results and device walk-through with multiple weekdays and an overlap. **GO** only when views use shared app data, not separate hard-coded examples.