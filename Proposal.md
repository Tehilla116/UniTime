# UniTime — Project Proposal

**Course:** BSE 3340 Mobile Application Development · Zambia University College of Technology  
**Status:** Draft for group and lecturer review · **Date:** 30 October 2026

> **Validation note:** This is a proposed problem to check with university and college students at more than one Zambian campus before submission. Do not present the current-workaround description as confirmed until students have verified it.

## 1. What specific problem are you solving?

Zambian university and college students may have to piece together weekly lectures and lab timetables from printed sheets, class-group messages, or later changes. When details are not easy to check in one place, students can miss a class or arrive at the wrong time or room. We will ask students from more than one campus whether this happens to them and which timetable information they need most.

## 2. How do people currently deal with this problem?

Our starting assumption is that students check a shared or printed timetable, class WhatsApp messages, or ask classmates when they need to confirm a class or a change. We will verify these actual habits with students at more than one Zambian university or college rather than claim they are true of all students.

## 3. Who exactly will use your app?

University and college students in Zambia who use an Android phone and want to keep their own weekly lectures and labs organised. UniTime is for an individual student's timetable, not for lecturers or university administrators.

## 4. What will your app do to solve the problem?

Students can add, edit, and remove a class with its title, day, start/end time, and venue; record a timetable change for themselves; see today's classes in time order; and check their weekly timetable for overlapping classes. The app saves entries locally so the timetable is available offline and after closing the app. Optional class reminders will be supported where notification permission is granted.

## 5. Describe your problem and solution in ONE sentence.

**UniTime helps university and college students in Zambia keep an editable, offline weekly class timetable, record personal schedule changes, and check upcoming classes and time clashes in one Android app.**

## 6. How is your idea different from a to-do list, a calculator, a student portal, or another common project?

UniTime focuses on one specific academic task for university and college students in Zambia: recurring class times and venues, with personal timetable changes and overlap checking. Unlike a to-do list it organises classes by weekday and time; unlike a generic student portal it does not attempt to manage grades, registration, or institution-wide announcements. These features are a focused student timetable tool, not a claim that no similar apps exist.

## Screens

### Screen 1: Today

Shows the current day and the student's saved classes in start-time order, including each class name, time, and venue. If there are no classes, it shows an empty-state message and an **Add class** button. The student can tap a class to edit it, tap **Add class** to open the form, or navigate to **Weekly Timetable** and **Settings**.

### Screen 2: Weekly Timetable

Shows the student's saved classes grouped by weekday, with each class's name, time, and venue. The student can choose a day, tap a class to edit it, or tap **Add class** to open the form with that day preselected. Overlapping classes are visibly flagged; the student can return to the form to adjust either entry. Navigation returns to **Today** or opens **Settings**.

### Screen 3: Add/Edit Class

A focused form for one class: class name, weekday, start time, end time, and optional venue. The student enters or selects these details, then taps **Save** or **Cancel**. Save returns to the screen they came from and shows the updated class. If validation fails, the form remains open and shows a clear message beside the relevant field.

### Screen 4: Settings

Shows the optional class-reminder setting and reminder lead time. The student can turn reminders on or off and choose a lead time; if Android notification permission is needed, the app explains why before asking. The student can return to **Today** or **Weekly Timetable**. Turning reminders off does not delete timetable entries.

## Complete user journey

The student opens **Today**, sees that no classes have been added, and taps **Add class**. On **Add/Edit Class**, they enter “Mobile App Development,” choose Monday, set 09:00–10:30, optionally enter the room, and tap **Save**. UniTime validates the details, saves the class locally, and returns to **Today** or **Weekly Timetable**, where the class appears under Monday; the student can close and reopen the app and see it still there.

## Form and validation

The **Add/Edit Class** screen contains the form. Class name, weekday, start time, and end time are required; venue is optional. The app checks that the name is not blank, times are valid, and the end time is later than the start time. If an entry overlaps another class on the same weekday, UniTime shows an overlap warning and lets the student review or save anyway. Example error messages: **“Enter a class name.”**, **“Choose a start and end time.”**, and **“End time must be after start time.”** Invalid entries are not saved, and focus stays on the form.

## Data saved after the app closes

UniTime saves each class's local ID, class name, weekday, start time, end time, and optional venue in a local database. It also saves the reminder-enabled setting and selected reminder lead time. This lets the timetable and reminder preference survive app closure and phone rotation; notification permission is managed separately by Android and may be denied by the student.

## Three most important things the first version must do

1. **Let a student add and manage class entries:** Save a class name, weekday, start and end time, and optional venue; allow editing and deleting entries, with clear validation for missing or invalid fields.
2. **Show a useful schedule:** Display today's classes in time order and the full weekly timetable, flagging same-day time overlaps so students can review possible clashes.
3. **Keep the schedule after closing the app:** Store timetable entries locally with Room so they remain available offline and after the app is reopened. Optional class notifications are the second researched concept, but should be implemented only after these core features work.

## Why this project is small enough to finish by 9 November 2026

We will build the smallest useful version first: one personal timetable on one Android device, with four focused screens (Today, Weekly Timetable, Add/Edit Class, and Settings), local-only storage, and no account or internet requirement. We will implement and test class entry and validation first, then the weekly/today views and overlap warning, then Room persistence and rotation/reopen behavior; the core app will remain usable without notifications if reminder permission or scheduling takes longer than expected. We will leave out login, cloud sync, lecturer/admin dashboards, importing official timetables, timetable sharing, grades/registration, maps, and advanced reminder scheduling.

## Research topic 1: Room local database

**Name:** Room local database (entities, DAOs, and queries)

**Why does UniTime need it?** UniTime must keep each student's classes after the app closes and make them available offline. Room will store the class name, weekday, start/end times, venue, and reminder settings in a structured local database so the Today and Weekly Timetable screens can load, update, and delete saved entries.

**Where will you learn it, and who will own it?** Start with the official [Android Developers Room guide](https://developer.android.com/training/data-storage/room), including its Kotlin sample code for entities, DAOs, and a database. **Interview owner: Tehilla Chiwama.** This member will implement or lead the Room feature, write the README Research Note (what it is, why UniTime needs it, source used, and what went wrong), and prepare to explain the data flow in the interview.

## Research topic 2: Android class reminder notifications

**Name:** Android notification channels and runtime notification permission

**Why does UniTime need it?** Students can opt in to receive a reminder before a saved class, so they do not have to keep opening the app to check what is coming next. UniTime will show the reminder setting only as an optional feature; if notification permission is denied, the timetable still works normally.

**Where will you learn it, and who will own it?** Start with the official [Android Developers guide to creating notifications](https://developer.android.com/develop/ui/compose/notifications/create-notification), its Kotlin sample code, the [notification channels guide](https://developer.android.com/develop/ui/compose/notifications/channels), and the [Android 13+ notification permission guide](https://developer.android.com/develop/ui/compose/notifications/notification-permission). **Interview owner: Mwenya Simpungwe.** This member will lead the opt-in notification feature, write its README Research Note (what it is, why UniTime needs it, sources used, and what went wrong), and prepare to demonstrate permission-denied handling.

## Group members

1. **Tehilla Chiwama**
2. **Frank Mwelwa**
3. **Bright Mwansa**
4. **Cheela Mulilo**
5. **Mwenya Simpungwe**

**Student numbers:** Add each member's student number before submission.

**Approval:** Confirm the problem and current workarounds with students from more than one campus, add all five student numbers, submit the proposal, and wait for the lecturer's **Approved** before continuing implementation.