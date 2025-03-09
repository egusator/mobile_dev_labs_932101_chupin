package com.example.app_deadline_manager.compose

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.app_deadline_manager.compose.create_task.TaskViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksListScreen(
    modifier: Modifier = Modifier,
    initialContent: List<TaskViewModel>,
    navController: NavController
) {
    var taskList by remember { mutableStateOf(initialContent) }
    var taskToRemove by remember { mutableStateOf<TaskViewModel?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val scrollState = rememberScrollState()
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                selectedDate = date
                showDatePicker = false
            }
        )
    }

    if (taskToRemove != null) {
        AlertDialog(
            onDismissRequest = { taskToRemove = null },
            title = { Text("Выполнено?") },
            text = { Text("Вы хотите удалить эту задачу?") },
            confirmButton = {
                TextButton(onClick = {
                    taskList = taskList - taskToRemove!!
                    taskToRemove = null
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToRemove = null }) {
                    Text("Нет")
                }
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
                onClick = { if (selectedDate > LocalDate.now()) selectedDate = selectedDate.minusDays(1) },
                enabled = selectedDate > LocalDate.now()
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Назад")
            }

            IconButton(
                onClick = { selectedDate = selectedDate.plusDays(1) }
            ) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Вперёд")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = taskList,
                key = { it.taskName }
            ) { task ->
                AnimatedVisibility(
                    visible = task in taskList,
                    exit = shrinkVertically() + fadeOut()
                ) {
                    TaskItem(
                        task = task,
                        onClick = { taskToRemove = task },
                        onDoubleClick = { taskList = taskList - task }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskItem(task: TaskViewModel, onClick: () -> Unit, onDoubleClick: () -> Unit) {
    var lastClickTime by remember { mutableStateOf(0L) }

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
            Text(text = task.taskName, fontWeight = MaterialTheme.typography.bodyLarge.fontWeight, fontSize = 18.sp)
            Text(text = "Описание: ${task.description}", fontSize = 14.sp)
            Text(text = "Дедлайн: ${task.deadline}", fontSize = 14.sp, color = Color.Red)
            Text(text = "Приоритет: ${task.priority}", fontSize = 14.sp, color = Color.Blue)
            Text(text = "Оценка времени: ${task.timeEstimate()}", fontSize = 14.sp)
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
