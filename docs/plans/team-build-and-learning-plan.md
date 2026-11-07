# UniTime — Updated Team Build and Submission Plan

**Updated:** Friday, 6 November 2026 (CAT)  
**Submission deadline / complete working-project deadline:** Monday, 9 November 2026 (working Android app, repository + README/research notes, Unlisted demo video, and peer ratings all finished and submitted)  
**Individual interviews:** Begin Monday, 16 November 2026  
**Status:** Time-compressed recovery plan; proposal approval must be confirmed before implementation.  
**Goal:** Use the remaining time to build the smallest complete, reproducible UniTime Android app and prepare all five members for contribution checks and individual interviews.

> **Deadline correction:** The project brief says the working app, GitHub repository with README/research notes, demo-video link, and peer ratings are all due **Monday, 9 November 2026**. The project must be complete by that date—not merely have its links submitted. The 3-minute maximum video must be Unlisted and show a running app; individual interviews start **Monday, 16 November**. The plan targets completion by Sunday 8 November to leave Monday as a verification/submission buffer; Sunday is an internal target, not the course deadline. Earlier dated work that has passed is archived, not an active schedule.

**Approval gate:** Do not start or continue implementation until the lecturer has approved the proposal. Approval has not been confirmed in the available records; verify it immediately.

## Verified current state and blockers

- **Fresh-project root (user confirmed):** `/Users/tehillachiwama/projects/UniTime/`. A new Kotlin/Compose Android project, Gradle wrapper, manifest, app module, and local Git repository have now been created there; previous scaffold at `/Users/tehillachiwama/Year 3/CampusConnectMVP/` remains untouched and is reference-only.
- **Lecturer approval:** user explicitly confirmed on 6 November that UniTime is approved.
- **Fresh project state (6 November):** `com.vibecoders.unitime` package, Today/Week/Add/Edit/Settings Compose screens, validation/overlap model, Room entity/DAO/database/repository, and baseline tests are implemented. Core screens install/run on Pixel 10 Pro XL API 37 emulator. Unit tests, debug build, and connected UI/database tests passed.
- **Still outstanding:** GitHub remote/member access and five individual commits; student numbers in proposal; README/research notes; notification feature; fresh-clone review; demo video and peer ratings. Room persists class entries; end-to-end close/reopen process was not yet verified (Activity recreation test passed).
- [x] Friday 6 November — optional reminders implemented with WorkManager, notification channel/permission flow and denial-safe settings; runtime delivery still needs demo evidence.
- [x] Saturday 7 November — replaced manual weekday entry and time typing with a weekday dropdown and scrollable hour/minute picker; connected journey test selects Friday, 22:35–22:40, and saves it.
- [x] Saturday 7 November — created UniTime clock/calendar logo, adaptive launcher icon and AndroidX splash-screen theme; connected branding smoke test passes.
- [ ] Remaining Day 5–7 checks: actual reminder delivery demo; README and researched notes; clean-clone rehearsal; shared member contributions; interview/demo/submission deliverables.

## Day 1–4 archived checklist status (reviewed 6 November; re-run 6 November after approval)

The original Day 1–4 sheets are retained under `archive/original-daily-checklists/`. The first snapshot recorded only what was known before approval. The user has since confirmed lecturer approval and directed a fresh Android project in this canonical folder; the existing `/Year 3/CampusConnectMVP/` scaffold remains untouched.

- **Day 1 — proposal:** [x] `Proposal.md` and `Proposal.pdf` are present; [x] all five names are listed. [ ] Student numbers remain missing. [ ] No written student interview notes or lecturer approval record have been added here; user confirmed verbally that approval was received.
- **Day 2 — kickoff/build gate:** [x] Fresh canonical Android/Compose project initialized; [x] local Git repository created; [ ] shared GitHub remote and all-five access/commits not set up. [x] Baseline Android SDK/Gradle commands verified in canonical root. [ ] Formal team agreement/device assignment not recorded.
- **Day 3 — navigation/model:** [x] `ClassEntry` validation and overlap rules implemented with unit tests; [x] Today, Weekly Timetable, Add/Edit, Settings screens and navigation implemented; [x] UI installed/launched on Pixel 10 Pro XL API 37 emulator and navigation instrumented test passed. [ ] Back navigation behavior not yet explicitly verified.
- **Day 4 — class form:** [x] Add/Edit Class form supports add/edit/delete and validates name, weekday, time range; [x] inline error messages and tests for valid/invalid times; [ ] manual CRUD flow and rotation while typing still need verification.

## Remaining acceptance work

Core screens and validation are implemented and tested in the fresh canonical project. **Room persistence is not integrated yet**; current runtime list uses in-memory state, so entries do not yet survive app close/reopen. Optional notifications, README/research notes, clean-clone rehearsal, demo video, and peer ratings remain. Continue TDD and complete these by the brief’s 9 November deadline; keep old scaffold untouched.

## Deadline facts and what must be submitted

From the assignment brief:

- **Monday 9 November 2026:** complete working Android app and one GitHub repository with README/research notes, plus demo-video link (YouTube Unlisted, 3 minutes maximum) and peer ratings.
- **From Monday 16 November 2026:** 15-minute individual interviews; each member must understand and explain the app and a researched concept.
- The required app includes at least three navigable screens, validated user input, a scrollable list using app data, lifecycle handling, local persistence that survives app close, separation of UI and data/logic, safe empty/bad-input/permission handling, fresh-clone build/run, and at least two researched concepts used in the app.

## Recovery scope: smallest acceptable UniTime MVP

**Core flow:** A student adds/edits/deletes their personal weekly classes (name, weekday, start/end time, optional venue), sees today and the full week in data-backed lists, receives a clear validation error for invalid entries, and retains saved classes across rotation and app restart using Room.

**Required research concepts:**

1. **Room local database** — actually use it for class entries and explain its entity/DAO/database flow in README. Owner: Tehilla Chiwama.
2. **Android notification channels and runtime notification permission** — implement an opt-in class reminder in Settings, demonstrate a reminder, and handle permission denial without breaking the timetable. Owner: Mwenya Simpungwe. Do not promise exact delivery timing; no exact-alarm permission.

**If time becomes critical:** Prioritize a complete and tested Room-backed timetable first. The assignment requires both researched concepts, so notification feature and note remain required, but keep reminders minimal: one optional lead-time choice, one channel, one tested reminder case, and an explicit permission-denied path. No login, cloud sync, official timetable import, institution-wide change publishing, date-specific cancellation system, grades, maps, chat, or sharing.

## Team ownership for the remaining work

| Member | Lead responsibility | Pair / reviewer |
|---|---|---|
| **Tehilla Chiwama** | Room entity/DAO/database/repository, persistence tests, Room research note. | Frank Mwelwa |
| **Frank Mwelwa** | Add/Edit Class form, field validation, errors, and CRUD tests. | Tehilla Chiwama |
| **Bright Mwansa** | Today + Weekly lists, sorting, empty state, overlap warning/tests. | Cheela Mulilo |
| **Cheela Mulilo** | Navigation/back behavior, screen integration, Settings UI and reminder preference. | Bright Mwansa |
| **Mwenya Simpungwe** | Opt-in notifications, Android channel/permission/denied behavior, notification research note. | Cheela Mulilo |

All five should obtain GitHub access immediately, review the shared `ClassEntry` fields/rules, and make meaningful code commits from their own accounts. Pair review and short teach-back are part of each slice; avoid leaving one member unable to explain another layer.

## Timeboxed recovery schedule

Dates are fixed to the actual remaining calendar. The daily work amounts below are proposed; confirm all five members’ availability immediately. Do not backdate tasks or mark an item complete without checking it.

### Friday 6 November — completed kickoff/build gate (user confirmed approval)

- [x] User explicitly confirmed lecturer approval on 6 November; the earlier approval gate is resolved.
- [x] Created a fresh Compose Android app in `/Users/tehillachiwama/projects/UniTime/`; old scaffold remains untouched.
- [x] Created local Git repository, selected package and app ID `com.vibecoders.unitime`, and used Pixel 10 Pro XL API 37 emulator for connected tests.
- [x] Implemented the first core slice: class model/validation/overlap rules; Today/Week/Add/Edit/Settings screens; Room entity/DAO/database/repository and persistence tests.
- [x] Verified `./gradlew :app:testDebugUnitTest :app:assembleDebug :app:connectedDebugAndroidTest` successfully. App installed/launched; navigation, validation, DB CRUD, file-backed database close/reopen, and Activity-recreation user journey pass.
- [ ] Still verify full app process force-stop/relaunch persistence through the UI and instrumented edit/delete journeys; connected Room close/reopen test proves database durability, not those complete UI workflows.
- [ ] Connect local Git repository to a shared GitHub remote and invite all five members; their own-account commits remain required.
- [ ] Add student IDs to proposal and record group decisions/README setup steps.

### Saturday 7 November — remaining core hardening and GitHub collaboration

- **Must be completed by the end of 7 November:** shared GitHub remote is connected, all five can clone/commit, edit/delete UI journeys are tested, and Room data is verified after a full app force-stop/relaunch (not only activity recreation).
- Frank: add instrumented edit/delete and malformed-time form journeys.
- Bright: verify multiple-day sorting, scrolling, empty state, and overlapping classes in emulator.
- Cheela: verify navigation and Android back behavior; inspect bottom bar in gesture-navigation mode.
- Tehilla: verify Room persists after process/app stop and relaunch using the actual app DB; connected Room DB close/reopen test is already green, but UI process-restart path remains.
- Mwenya: implement or spike optional notification channel/permission flow after core stabilization.
- Each code change is test-first; run focused test, regression, and commit under member’s own account.
- **Gate/evidence:** GitHub remote/access, local unit + connected tests, actual app close/relaunch proof, validated CRUD path, screenshots or test report.

### Sunday 8 November — notifications, README, demo, peer ratings


- **Must be complete by the end of 8 November:** all required app features, two implemented researched concepts and README Research Notes, fresh-clone instructions, peer ratings, and a working <= 3-minute Unlisted demo. This is the team’s safety buffer. The **course deadline is 9 November**; Monday remains available to finish any truly remaining requirement, verify everything, and submit—do not treat it as the original plan’s links-only day.
- Mwenya + Cheela: complete one opt-in reminder workflow, notification permission request, and denial-safe behavior. Verify on emulator/device; do not add exact alarm permissions.
- Tehilla + Frank: complete Room tests and a roughly half-page README research note: what Room is, why needed, official docs/sample used, what went wrong.
- Mwenya + Bright: complete notification README note with the same four required items; record permission/timing limitation.
- Bright + Cheela: run end-to-end screen, validation, overlap, rotation, close/reopen checks; fix acceptance blockers only.
- Frank: lead README build/run steps and fresh-clone rehearsal. Use only actual project-root commands; do not invent repo URLs.
- All five: record a short demo that meets the brief (problem first 30s, running user journey, one researched concept, <=3 minutes, Unlisted); complete peer ratings and verify ownership links.
- **Gate/evidence:** both research concepts used in the app; README with run steps and both notes; clean-clone/build proof; video link opens and duration <= 3 minutes; peer-rating status known. Feature freeze by end of day.

### Monday 9 November — final verification and submission only

- No planned new features. Clean-clone build using README; verify at least three navigable screens, add/edit validation, scrollable class list, Room data after app restart, and notification permission denied without crash.
- Verify every member has a meaningful commit from their own account, README includes both research notes and named official sources, and the video is Unlisted and no longer than 3 minutes.
- Complete peer ratings. Submit repository link, video link, and peer ratings by the course deadline; save confirmation.
- **If a blocker appears:** revert the last risky change or make only the smallest fix; rerun affected checks and record the actual outcome. No feature expansion.

### 10–15 November — individual interview preparation

- All five study the class/data flow and the two researched concepts; rehearse one change live (e.g. add a field and trace model → Room → form → list).
- Tehilla leads a Room walkthrough; Mwenya leads notification/permission walkthrough. Every other member must also be able to explain both at a basic level.
- Review GitHub history and each member’s own code contribution. Do not claim a person built or researched a feature unless they can explain and demonstrate it.

### From Monday 16 November — interviews

Each member attends their allocated individual slot with the project open and demonstrates code behavior directly. Use the actual repository and the actual submitted code. Be ready to explain rotation/persistence, screen/data separation, a form field change, a researched feature, and what broke during implementation.

## Active deadline checklists

The plan was revised on Friday 6 November because the original dated checklists assumed work completed before the actual planning update. Use only these active sheets for the remaining deadline window:

- [6 November — approval, repo, and scope gate](daily-checklists/2026-11-06-approval-repo-gate.md)
- [7 November — core timetable and Room](daily-checklists/2026-11-07-core-timetable-room.md)
- [8 November — notifications, README, demo, peer ratings](daily-checklists/2026-11-08-finalize-demo.md)
- [9 November — submission deadline](daily-checklists/2026-11-09-submission.md)

Superseded first-draft checklists are archived at `archive/original-daily-checklists/`; they are historical only and should not be followed as the current schedule.

## Final submission checklist
- [ ] Lecturer approval confirmed before implementation.
- [ ] GitHub repository exists, is cloneable, and all five members have meaningful commits from their own accounts.
- [ ] App builds/runs from a fresh clone following README.
- [ ] At least three real screens navigate and back behavior is sensible.
- [ ] Add/Edit form validates required fields and clearly explains invalid values.
- [ ] Scrollable list displays app-owned class data and empty state.
- [ ] Room persistence retains class entries across rotation and app restart.
- [ ] At least two self-researched concepts are implemented and explained in README notes: Room (Tehilla) and notification channels/permission (Mwenya).
- [ ] Empty state, invalid input, and denied notification permission do not crash.
- [ ] Demo shows app running, problem in first 30 seconds, real journey, and one researched feature; Unlisted and <=3 minutes.
- [ ] Peer ratings are completed and submitted by 9 November.
- [ ] Submission confirmation and working repository/video links are saved.
- [ ] All five members prepare for individual interviews starting 16 November.

## Risks and immediate decisions

- **Approval unknown / implementation gate:** resolve with lecturer immediately; do not build until approved.
- **Wrong/missing canonical Android source:** user’s canonical folder currently contains only proposal and plan, while the Android project is in `Year 3/CampusConnectMVP`. Agree and document the actual repo root and GitHub migration today.
- **Only three calendar days remain before the 9 November due date:** limit to the requirements above, hold daily integration, skip visual polish and nonessential functionality, and ask the lecturer promptly if the deadline is no longer feasible.
- **Notification timing/permission:** keep it opt-in and minimal, test denial, describe limits honestly.
- **Uneven ownership:** pair every slice and make all five complete commits and teach-back; preserve interview prep after submission.
