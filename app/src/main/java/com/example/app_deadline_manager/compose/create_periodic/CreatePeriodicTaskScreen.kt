package com.example.app_deadline_manager.compose.create_periodic

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreatePeriodicTaskScreen(
    modifier: Modifier = Modifier,
    createPeriodicTaskViewModel: CreatePeriodicTaskViewModel
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("как.", "когда.", "готово.")

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTabIndex) {
                0 -> FillInfo(createPeriodicTaskViewModel)
                1 -> FillPeriods(createPeriodicTaskViewModel)
                2 -> ShowSummary(
                    createPeriodicTaskViewModel
                 )
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
fun FillInfo(viewModel: CreatePeriodicTaskViewModel) {
    var name by remember { mutableStateOf(viewModel.taskName) }
    var description by remember { mutableStateOf(viewModel.taskDescription) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = {
                name = it
                viewModel.taskName = it
            },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = description,
            onValueChange = {
                description = it
                viewModel.taskDescription = it
            },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

enum class DayOfWeek(val displayName: String) {
    MONDAY("Понедельник"),
    TUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FillPeriods(viewModel: CreatePeriodicTaskViewModel) {
    var periods by remember { mutableStateOf(viewModel.workPeriods) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Укажите периоды", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(periods.sortedBy { it.day.ordinal }) { period ->
                WorkPeriodItem(
                    period = period,
                    onRemove = {
                        periods = periods - period
                        viewModel.workPeriods = periods
                    },
                    onUpdateTime = { newStart, newEnd ->
                        periods = periods.map {
                            if (it.day == period.day) it.copy(startTime = newStart, endTime = newEnd)
                            else it
                        }
                        viewModel.workPeriods = periods
                    }
                )
            }

            if (periods.size < DayOfWeek.values().size) {
                item {
                    AddPeriodButton(
                        availableDays = DayOfWeek.values().toList().minus(periods.map { it.day }),
                        onAddPeriod = { newPeriod ->
                            periods = periods + newPeriod
                            viewModel.workPeriods = periods
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkPeriodItem(
    period: WorkPeriod,
    onRemove: () -> Unit,
    onUpdateTime: (LocalTime, LocalTime) -> Unit
) {
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = period.day.displayName, style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { showStartTimePicker = true }) {
                    Text("Начало: ${period.startTime}")
                }
                Button(onClick = { showEndTimePicker = true }) {
                    Text("Конец: ${period.endTime}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onError)
            ) {
                Text("Удалить", color = MaterialTheme.colorScheme.onError)
            }
        }
    }

    val context = LocalContext.current

    if (showStartTimePicker) {
        showTimePicker(context, period.startTime) { newStartTime ->
            onUpdateTime(newStartTime, period.endTime)
            showStartTimePicker = false
        }
    }

    if (showEndTimePicker) {
        showTimePicker(context, period.endTime) { newEndTime ->
            onUpdateTime(period.startTime, newEndTime)
            showEndTimePicker = false
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPeriodButton(
    availableDays: List<DayOfWeek>,
    onAddPeriod: (WorkPeriod) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить период")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableDays.forEach { day ->
                DropdownMenuItem(
                    onClick = {
                        onAddPeriod(WorkPeriod(day, LocalTime.of(9, 0), LocalTime.of(18, 0)))
                        expanded = false
                    },
                    text = { Text(day.displayName) } // <-- Добавлен text
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun showTimePicker(
    context: Context,
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val hour = initialTime.hour
    val minute = initialTime.minute

    val timePickerDialog = TimePickerDialog(
        context,
        { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            onTimeSelected(LocalTime.of(selectedHour, selectedMinute))
        },
        hour,
        minute,
        true
    )
    timePickerDialog.show()
}

@Composable
fun ShowSummary(viewModel: CreatePeriodicTaskViewModel) {
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

                Text("Название: ${viewModel.taskName}", style = MaterialTheme.typography.bodyLarge)
                Text("Описание: ${viewModel.taskDescription}", style = MaterialTheme.typography.bodyLarge)
                Text("Расписание:", style = MaterialTheme.typography.bodyLarge)

                Text(
                    viewModel.getFormattedSchedule()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Готово")
                }
            }
        }
    }
}