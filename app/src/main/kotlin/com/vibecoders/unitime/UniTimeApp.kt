package com.vibecoders.unitime

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class Route { TODAY, WEEK, FORM, SETTINGS }

private const val KEY_REMINDERS_ENABLED = "reminders_enabled"
private const val KEY_REMINDER_LEAD_MINUTES = "reminder_lead_minutes"

@Composable
fun UniTimeApp(viewModel: TimetableViewModel) {
    val entries by viewModel.entries.collectAsState()
    var route by rememberSaveable { mutableStateOf(Route.TODAY.name) }
    var editingId by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val preferences = context.getSharedPreferences("unitime_settings", android.content.Context.MODE_PRIVATE)
    val permissionGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    var reminderEnabled by remember(preferences) { mutableStateOf(preferences.getBoolean(KEY_REMINDERS_ENABLED, false)) }
    var reminderLeadMinutes by remember(preferences) {
        mutableIntStateOf(preferences.getInt(KEY_REMINDER_LEAD_MINUTES, 15).takeIf { it == 5 || it == 15 || it == 30 } ?: 15)
    }
    var formError by rememberSaveable { mutableStateOf<String?>(null) }
    val selectedRoute = Route.valueOf(route)
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        reminderEnabled = granted
        preferences.edit().putBoolean(KEY_REMINDERS_ENABLED, granted).apply()
        if (granted) entries.forEach { viewModel.scheduleReminder(it, reminderLeadMinutes) }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner, preferences) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val enabled = preferences.getBoolean(KEY_REMINDERS_ENABLED, false)
                val lead = preferences.getInt(KEY_REMINDER_LEAD_MINUTES, 15).takeIf { it == 5 || it == 15 || it == 30 } ?: 15
                val allowed = permissionGranted && NotificationManagerCompat.from(context).areNotificationsEnabled()
                if (enabled != reminderEnabled || lead != reminderLeadMinutes || reminderEnabled != allowed) {
                    reminderEnabled = enabled && allowed
                    reminderLeadMinutes = lead
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(bottomBar = {
        Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(onClick = { route = Route.TODAY.name }) { Text("Today") }
            OutlinedButton(onClick = { route = Route.WEEK.name }) { Text("Week") }
            OutlinedButton(onClick = { editingId = null; formError = null; route = Route.FORM.name }) { Text("Add class") }
            OutlinedButton(onClick = { route = Route.SETTINGS.name }) { Text("Settings") }
        }
    }) { padding ->
        when (selectedRoute) {
            Route.TODAY -> TodayScreen(entries, onEdit = { editingId = it.id; route = Route.FORM.name }, modifier = Modifier.padding(padding))
            Route.WEEK -> WeekScreen(entries, onEdit = { editingId = it.id; route = Route.FORM.name }, modifier = Modifier.padding(padding))
            Route.FORM -> ClassFormScreen(
                existing = entries.firstOrNull { it.id == editingId },
                errorMessage = formError,
                onSave = { title, weekday, start, end, venue ->
                    val result = validateClassEntry(title, weekday, start, end, venue, editingId ?: java.util.UUID.randomUUID().toString())
                    if (result.isValid) {
                        viewModel.save(requireNotNull(result.entry))
                        if (reminderEnabled) viewModel.scheduleReminder(requireNotNull(result.entry), reminderLeadMinutes)
                        else viewModel.cancelReminder(requireNotNull(result.entry).id)
                        formError = null
                        route = Route.WEEK.name
                    } else {
                        formError = result.errors.first().toUserMessage()
                    }
                },
                onDelete = { id -> viewModel.delete(id); editingId = null; formError = null; route = Route.WEEK.name },
                onCancel = { formError = null; route = if (editingId == null) Route.WEEK.name else Route.WEEK.name },
                modifier = Modifier.padding(padding)
            )
            Route.SETTINGS -> SettingsScreen(
                reminderEnabled = reminderEnabled,
                onReminderEnabledChange = { enabled ->
                    if (enabled && !permissionGranted) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    else {
                        reminderEnabled = enabled
                        preferences.edit().putBoolean(KEY_REMINDERS_ENABLED, enabled).apply()
                        if (enabled) entries.forEach { viewModel.scheduleReminder(it, reminderLeadMinutes) }
                        else entries.forEach { viewModel.cancelReminder(it.id) }
                    }
                },
                leadMinutes = reminderLeadMinutes,
                onLeadMinutesChange = { lead ->
                    reminderLeadMinutes = lead
                    preferences.edit().putInt(KEY_REMINDER_LEAD_MINUTES, lead).apply()
                    if (reminderEnabled) entries.forEach { viewModel.scheduleReminder(it, lead) }
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

private fun ClassEntryError.toUserMessage(): String = when (this) {
    ClassEntryError.EMPTY_TITLE -> "Enter a class name."
    ClassEntryError.INVALID_WEEKDAY -> "Choose a weekday."
    ClassEntryError.INVALID_START_TIME, ClassEntryError.INVALID_END_TIME -> "Choose a valid class time."
    ClassEntryError.END_NOT_AFTER_START -> "End time must be after start time."
}

@Composable
private fun TodayScreen(entries: List<ClassEntry>, onEdit: (ClassEntry) -> Unit, modifier: Modifier = Modifier) {
    val day = Weekday.entries[LocalDate.now().dayOfWeek.value - 1]
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Today", style = MaterialTheme.typography.headlineMedium)
        Text(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM")), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        val today = entries.filter { it.weekday == day }.sortedBy { it.startMinute }
        if (today.isEmpty()) Text("No classes today. Add a class to start your timetable.")
        today.forEach { entry -> ClassCard(entry, entries, onEdit) }
    }
}

@Composable
private fun WeekScreen(entries: List<ClassEntry>, onEdit: (ClassEntry) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
        Text("Weekly timetable", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        if (entries.isEmpty()) Text("No classes yet. Add your first class.")
        Weekday.entries.forEach { day ->
            val dayEntries = entries.filter { it.weekday == day }.sortedBy { it.startMinute }
            if (dayEntries.isNotEmpty()) {
                Text(day.displayName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(top = 12.dp, bottom = 6.dp))
                dayEntries.forEach { ClassCard(it, entries, onEdit) }
            }
        }
    }
}

@Composable
private fun ClassCard(entry: ClassEntry, allEntries: List<ClassEntry>, onEdit: (ClassEntry) -> Unit) {
    val clashes = allEntries.any { it.id != entry.id && entriesOverlap(entry, it) }
    Card(onClick = { onEdit(entry) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(14.dp)) {
            Text(entry.title, style = MaterialTheme.typography.titleMedium)
            Text("${formatTime(entry.startMinute)}–${formatTime(entry.endMinute)}${if (entry.venue.isBlank()) "" else " · ${entry.venue}"}")
            if (clashes) Text("Time clash — check this class", color = MaterialTheme.colorScheme.error)
        }
    }
}

private fun formatTime(value: Int) = "%02d:%02d".format(value / 60, value % 60)

@Composable
private fun ClassFormScreen(
    existing: ClassEntry?, errorMessage: String?,
    onSave: (String, Weekday?, Int, Int, String) -> Unit,
    onDelete: (String) -> Unit, onCancel: () -> Unit, modifier: Modifier = Modifier
) {
    var title by rememberSaveable(existing?.id) { mutableStateOf(existing?.title.orEmpty()) }
    var weekdayName by rememberSaveable(existing?.id) { mutableStateOf(existing?.weekday?.name ?: Weekday.MONDAY.name) }
    var weekdayMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var start by rememberSaveable(existing?.id) { mutableStateOf(existing?.startMinute?.let(::formatTime) ?: "09:00") }
    var end by rememberSaveable(existing?.id) { mutableStateOf(existing?.endMinute?.let(::formatTime) ?: "10:00") }
    var timePickerTarget by rememberSaveable { mutableStateOf<String?>(null) }
    var pickerHour by rememberSaveable { mutableIntStateOf(9) }
    var pickerMinute by rememberSaveable { mutableIntStateOf(0) }
    var venue by rememberSaveable(existing?.id) { mutableStateOf(existing?.venue.orEmpty()) }
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(if (existing == null) "Add class" else "Edit class", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(title, { title = it }, label = { Text("Class name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        androidx.compose.foundation.layout.Box {
            OutlinedButton(
                onClick = { weekdayMenuExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Weekday (Monday–Sunday): ${Weekday.entries.first { it.name == weekdayName }.displayName}")
            }
            DropdownMenu(
                expanded = weekdayMenuExpanded,
                onDismissRequest = { weekdayMenuExpanded = false }
            ) {
                Weekday.entries.forEach { day ->
                    DropdownMenuItem(
                        text = { Text(day.displayName) },
                        onClick = {
                            weekdayName = day.name
                            weekdayMenuExpanded = false
                        }
                    )
                }
            }
        }
        OutlinedButton(
            onClick = {
                val current = parseTime(start).coerceAtLeast(0)
                pickerHour = current / 60
                pickerMinute = current % 60
                timePickerTarget = "start"
            },
            modifier = Modifier.fillMaxWidth().testTag("start-time-picker")
        ) { Text("Start time: ${formatTime(parseTime(start).coerceAtLeast(0))}") }
        OutlinedButton(
            onClick = {
                val current = parseTime(end).coerceAtLeast(0)
                pickerHour = current / 60
                pickerMinute = current % 60
                timePickerTarget = "end"
            },
            modifier = Modifier.fillMaxWidth().testTag("end-time-picker")
        ) { Text("End time: ${formatTime(parseTime(end).coerceAtLeast(0))}") }
        OutlinedTextField(venue, { venue = it }, label = { Text("Venue (optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { onSave(title, Weekday.entries.firstOrNull { it.name == weekdayName }, parseTime(start), parseTime(end), venue) }) { Text("Save") }
            OutlinedButton(onClick = onCancel) { Text("Cancel") }
            if (existing != null) OutlinedButton(onClick = { onDelete(existing.id) }) { Text("Delete") }
        }
    }
    timePickerTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { timePickerTarget = null },
            title = { Text(if (target == "start") "Choose start time" else "Choose end time") },
            text = {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    LazyColumn(modifier = Modifier.weight(1f).height(220.dp).testTag("hour-options")) {
                        items((0..23).toList(), key = { "hour-$it" }) { hour ->
                            val label = "%02d".format(hour)
                            Text(
                                label,
                                modifier = Modifier.fillMaxWidth().clickable { pickerHour = hour }.testTag("hour-option-$hour").padding(12.dp),
                                color = if (pickerHour == hour) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    LazyColumn(modifier = Modifier.weight(1f).height(220.dp).testTag("minute-options")) {
                        items((0..59).toList(), key = { "minute-$it" }) { minute ->
                            val label = "%02d".format(minute)
                            Text(
                                label,
                                modifier = Modifier.fillMaxWidth().clickable { pickerMinute = minute }.testTag("minute-option-$minute").padding(12.dp),
                                color = if (pickerMinute == minute) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val value = formatTime(pickerHour * 60 + pickerMinute)
                    if (target == "start") start = value else end = value
                    timePickerTarget = null
                }) { Text("Set time") }
            },
            dismissButton = { OutlinedButton(onClick = { timePickerTarget = null }) { Text("Cancel") } }
        )
    }
}

private fun parseTime(raw: String): Int {
    val match = Regex("^(\\d{1,2}):(\\d{2})$").matchEntire(raw) ?: return -1
    val hour = match.groupValues[1].toInt()
    val minute = match.groupValues[2].toInt()
    return if (hour in 0..23 && minute in 0..59) hour * 60 + minute else -1
}

@Composable
private fun SettingsScreen(
    reminderEnabled: Boolean,
    onReminderEnabledChange: (Boolean) -> Unit,
    leadMinutes: Int,
    onLeadMinutesChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        onReminderEnabledChange(granted)
    }
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Text("Turn on reminders to get a notification before a saved class. Android may delay delivery to save battery.")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Class reminders")
            Switch(
                checked = reminderEnabled,
                onCheckedChange = { enabled ->
                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                    ) permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    else onReminderEnabledChange(enabled)
                }
            )
        }
        Text("Reminder lead time")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(5, 15, 30).forEach { minutes ->
                val label = "$minutes min"
                if (leadMinutes == minutes) Button(onClick = { onLeadMinutesChange(minutes) }) { Text(label) }
                else OutlinedButton(onClick = { onLeadMinutesChange(minutes) }) { Text(label) }
            }
        }
        Text("If notification permission is denied, the timetable still works normally.")
    }
}
