package com.example.app_deadline_manager.compose.create_periodic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalTime


class CreatePeriodicTaskViewModel : ViewModel() {
    var taskName by mutableStateOf("")
    var taskDescription by mutableStateOf("")
    var workPeriods by mutableStateOf<List<WorkPeriod>>(emptyList())

    fun getFormattedSchedule(): String {
        return if (workPeriods.isEmpty()) {
            "Не указано"
        } else {
            workPeriods.sortedBy { it.day.ordinal }
                .joinToString("\n") { "${it.day.displayName}: ${it.startTime} - ${it.endTime}" }
        }
    }
}

data class WorkPeriod(
    val day: DayOfWeek,
    val startTime: LocalTime,
    val endTime: LocalTime
)