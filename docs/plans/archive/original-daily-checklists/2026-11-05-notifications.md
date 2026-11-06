# Thursday 5 November 2026 — Optional class reminders

## Objective
Implement the second researched concept: opt-in class reminders, with permission-denied behavior that leaves the timetable usable.

## Owners
Mwenya leads the notification feature and Research Topic 2; Cheela integrates Settings; Tehilla reviews stored preferences; Frank checks controls/error wording; Bright verifies correct class/time content.

## Checklist
- [ ] Study official Android notification creation, channels, and Android 13+ runtime permission guides and Kotlin sample code.
- [ ] Request permission only after the user opts in; explain the purpose before the system prompt.
- [ ] Create a channel and schedule/cancel a reminder when a class changes or is deleted; avoid duplicate scheduled work.
- [ ] Test permission granted and denied; denied permission must not crash or prevent add/list/edit/save.
- [ ] Do not request exact alarms; document that background delivery may be delayed by Android.
- [ ] Draft Mwenya's Research Note with sources, need, actual issue encountered, and permission-denied handling.
- [ ] Run focused tests and both Gradle verification commands; record outcomes.

## Evidence and gate
Evidence: on-device/emulator reminder, permission-denied walkthrough, research note. If reliable reminders exceed scope, retain the opt-in concept with a clearly limited demo and inform the lecturer; do not claim precise delivery.