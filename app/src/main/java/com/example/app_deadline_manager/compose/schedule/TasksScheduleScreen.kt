package com.example.app_deadline_manager.compose.schedule

import ScheduleState
import ScheduleViewModel
import TaskForChoose
import android.app.TimePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_deadline_manager.mapper.TaskForScheduleMapper
import com.example.app_deadline_manager.model.TaskForScheduleModel
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScheduleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ScheduleViewModel,
    taskMapper: TaskForScheduleMapper
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTaskCreationDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var taskOptions by remember { mutableStateOf<List<TaskForChoose>>(emptyList()) }

    val scheduleState by viewModel.scheduleState
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    // Загружаем список задач один раз при старте
    LaunchedEffect(Unit) {
        taskOptions = viewModel.getAllTasksForChoose()
    }

    LaunchedEffect(selectedDate) {
        println("🔄 Обновление расписания на дату: $selectedDate")
        viewModel.fetchSchedule(selectedDate)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(2.dp, Color.Gray, MaterialTheme.shapes.medium)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { showDatePicker = true }) {
                Text(text = selectedDate.format(dateFormatter), fontSize = 20.sp)
            }

            IconButton(
                onClick = {
                    if (selectedDate > LocalDate.now()) {
                        selectedDate = selectedDate.minusDays(1)
                    }
                },
                enabled = selectedDate > LocalDate.now()
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            IconButton(
                onClick = {
                    selectedDate = selectedDate.plusDays(1)
                }
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Вперёд")
            }
        }

        when (scheduleState) {
            is ScheduleState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is ScheduleState.Loaded -> {
                val schedule = (scheduleState as ScheduleState.Loaded).schedule

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(schedule, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onClick = {},
                            onDoubleClick = { viewModel.removeTask(task.id) },
                            taskMapper = taskMapper
                        )
                    }
                }
            }

            is ScheduleState.Error -> {
                val errorMessage = (scheduleState as ScheduleState.Error).message
                Text(text = "Ошибка: $errorMessage")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(
    task: TaskForScheduleModel,
    taskMapper: TaskForScheduleMapper, // Передаём маппер
    onClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    var lastClickTime by remember { mutableStateOf(0L) }

    val taskViewUi = remember { taskMapper.mapToTaskView(task) } // Маппинг данных

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime < 300) {
                    onDoubleClick()
                } else {
                    onClick()
                }
                lastClickTime = currentTime
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = taskViewUi.taskName, fontWeight = MaterialTheme.typography.bodyLarge.fontWeight, fontSize = 18.sp)
            Text(text = taskViewUi.workStart, fontSize = 14.sp)
            Text(text = taskViewUi.workEnd, fontSize = 14.sp)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val confirmEnabled = datePickerState.selectedDateMillis != null

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val selectedDate =
                            Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateSelected(selectedDate)
                    }
                },
                enabled = confirmEnabled
            ) {
                Text("Выбрать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCreationDialog(
    onDismiss: () -> Unit,
    onCreateTask: (Int?, LocalTime, LocalTime, LocalDate) -> Unit,
    taskOptions: List<TaskForChoose>,
    selectedDate: LocalDate
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskForChoose?>(null) }
    var workStart by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var workEnd by remember { mutableStateOf(LocalTime.of(10, 0)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Создание задачи") },
        text = {
            Column {
                Text("Выберите задачу")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedTask?.name ?: "Выберите задачу",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        taskOptions.forEach { task ->
                            DropdownMenuItem(
                                text = { Text(task.name) },
                                onClick = {
                                    selectedTask = task
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TimePicker(label = "Начало работы", selectedTime = workStart) {
                    workStart = it
                }

                Spacer(modifier = Modifier.height(8.dp))

                TimePicker(label = "Конец работы", selectedTime = workEnd) {
                    workEnd = it
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCreateTask(selectedTask?.id, workStart, workEnd, selectedDate)
                    onDismiss()
                },
                enabled = selectedTask != null
            ) {
                Text("Создать")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePicker(
    label: String,
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                onTimeSelected(LocalTime.of(hourOfDay, minute))
                showDialog = false
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        ).show()
    }

    Text(
        text = "$label: ${selectedTime.toString()}",
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(8.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}