package com.example.app_deadline_manager.compose.settings

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, modifier: Modifier = Modifier) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val email by viewModel.email
    val isPushEnabled by viewModel.isPushEnabled
    val isEmailNotificationsEnabled by viewModel.isEmailNotificationsEnabled
    val workStartTime by viewModel.workStartTime
    val workEndTime by viewModel.workEndTime

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val context = LocalContext.current

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Электронная почта") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Checkbox(
                checked = isPushEnabled,
                onCheckedChange = { viewModel.togglePushNotifications() }
            )
            Text("Отправлять push-уведомления", modifier = Modifier.padding(start = 8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Checkbox(
                checked = isEmailNotificationsEnabled,
                onCheckedChange = { viewModel.toggleEmailNotifications() }
            )
            Text("Отправлять уведомления по электронной почте", modifier = Modifier.padding(start = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Выбор времени начала рабочего дня
        Text("Начало рабочего дня:", style = MaterialTheme.typography.bodyLarge)
        Button(
            onClick = { showStartTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(workStartTime.format(timeFormatter))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Выбор времени конца рабочего дня
        Text("Конец рабочего дня:", style = MaterialTheme.typography.bodyLarge)
        Button(
            onClick = { showEndTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(workEndTime.format(timeFormatter))
        }
    }

    // TimePicker диалоги
    if (showStartTimePicker) {
        showTimePicker(context, workStartTime) { newTime ->
            viewModel.updateWorkStartTime(newTime)
            showStartTimePicker = false
        }
    }

    if (showEndTimePicker) {
        showTimePicker(context, workEndTime) { newTime ->
            viewModel.updateWorkEndTime(newTime)
            showEndTimePicker = false
        }
    }
}

// Функция отображения TimePicker
@RequiresApi(Build.VERSION_CODES.O)
fun showTimePicker(
    context: Context,
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val hour = initialTime.hour
    val minute = initialTime.minute

    TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
        },
        hour,
        minute,
        true // true = 24-часовой формат
    ).show()
}