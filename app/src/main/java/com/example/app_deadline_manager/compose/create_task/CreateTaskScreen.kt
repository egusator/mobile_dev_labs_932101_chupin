package com.example.app_deadline_manager.compose.create_task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskScreen(modifier: Modifier = Modifier, createTaskViewModel: TaskViewModel) {
    val tabs = listOf("что.", "инфо.", "готово.")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        TabRow(selectedTabIndex = selectedTabIndex, modifier = Modifier.fillMaxWidth()) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTabIndex) {
                0 -> DescriptionScreen(createTaskViewModel)
                1 -> TaskInfoScreen(createTaskViewModel)
                2 -> ShowSummary(createTaskViewModel)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { if (selectedTabIndex > 0) selectedTabIndex-- },
                enabled = selectedTabIndex > 0
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            IconButton(
                onClick = { if (selectedTabIndex < tabs.size - 1) selectedTabIndex++ },
                enabled = selectedTabIndex < tabs.size - 1
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Вперёд")
            }
        }
    }
}

@Composable
fun DescriptionScreen(viewModel: TaskViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = viewModel.taskName,
            onValueChange = { viewModel.taskName = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskInfoScreen(viewModel: TaskViewModel) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = viewModel.deadline.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Дедлайн", style = MaterialTheme.typography.titleMedium)
        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.width(280.dp)
        ) {
            Text(
                text = datePickerState.selectedDateMillis?.let { date ->
                    val formatter = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
                    formatter.format(Date(date))
                } ?: "Выберите дату"
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { date ->
                                viewModel.deadline = LocalDate.from(Instant.ofEpochMilli(date))
                            }
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Отмена")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Text(text = "Приоритет", style = MaterialTheme.typography.titleMedium)
        PrioritySelector(
            selectedPriority = viewModel.priority.ifEmpty { "Выбор..." },
            onPriorityChange = { viewModel.priority = it }
        )

        Text(text = "Оценка по времени", style = MaterialTheme.typography.titleMedium)
        TimePicker(
            hours = viewModel.hours,
            minutes = viewModel.minutes,
            onTimeChange = { h, m ->
                viewModel.hours = h
                viewModel.minutes = m
            }
        )
    }
}

@Composable
fun PrioritySelector(selectedPriority: String, onPriorityChange: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedButton(onClick = { showDialog = true }) {
        Text(text = selectedPriority)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Выберите приоритет") },
            text = {
                Column {
                    listOf("Высокий", "Средний", "Низкий").forEach { priority ->
                        OutlinedButton(
                            onClick = {
                                onPriorityChange(priority)
                                showDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = priority)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun TimePicker(hours: Int, minutes: Int, onTimeChange: (Int, Int) -> Unit) {
    var selectedHours by remember { mutableStateOf(hours) }
    var selectedMinutes by remember { mutableStateOf(minutes) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        NumberSelector("Часы", selectedHours, 0..23) { selectedHours = it }
        Spacer(modifier = Modifier.width(16.dp))
        NumberSelector("Минуты", selectedMinutes, 0..59) { selectedMinutes = it }
    }

    LaunchedEffect(selectedHours, selectedMinutes) {
        onTimeChange(selectedHours, selectedMinutes)
    }
}

@Composable
fun NumberSelector(label: String, value: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > range.first) onValueChange(value - 1) }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Уменьшить")
            }
            Text(text = value.toString())
            IconButton(onClick = { if (value < range.last) onValueChange(value + 1) }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Увеличить")
            }
        }
    }
}

@Composable
fun ShowSummary(viewModel: TaskViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Сводка", style = MaterialTheme.typography.titleLarge)
                Text("Название: ${viewModel.taskName}")
                Text("Описание: ${viewModel.description}")
                Text("Дедлайн: ${viewModel.deadline}")
                Text("Приоритет: ${viewModel.priority}")
                Text("Оценка по времени: ${viewModel.hours}ч ${viewModel.minutes}м")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { }) {
                    Text("Готово")
                }
            }
        }
    }
}