package com.example.app_deadline_manager.compose.schedule

import ScheduleViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_deadline_manager.mapper.TaskForScheduleMapper
import com.example.app_deadline_manager.model.TaskForScheduleModel
import java.time.Instant
import java.time.LocalDate
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
    var taskToRemove: TaskForScheduleModel? by remember { mutableStateOf(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val scheduleState by viewModel.scheduleState
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    LaunchedEffect(Unit) {
        viewModel.fetchSchedule(selectedDate)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                selectedDate = date
                viewModel.fetchSchedule(date)
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
                        viewModel.fetchSchedule(selectedDate)
                    }
                },
                enabled = selectedDate > LocalDate.now()
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            IconButton(
                onClick = {
                    selectedDate = selectedDate.plusDays(1)
                    viewModel.fetchSchedule(selectedDate)
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
                    items(
                        items = schedule,
                        key = { it.id.toString() + it.date.toString() }
                    ) { task ->
                        AnimatedVisibility(
                            visible = task in schedule,
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            TaskItem(
                                task = task,
                                onClick = { taskToRemove = task },
                                onDoubleClick = { taskToRemove?.let { viewModel.removeTask(it.id) } },
                                taskMapper = taskMapper
                            )
                        }
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