package com.example.app_deadline_manager.compose.create_task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class TaskViewModel(
    var taskName: String = "",
    var description: String = "",
    var deadline: LocalDate = LocalDate.now(),
    var priority: String = "",
    var hours: Int = 0,
    var minutes: Int = 0
) {
    fun timeEstimate(): String {
        val hoursPart = if (hours > 0) "$hours ч" else ""
        val minutesPart = if (minutes > 0) "$minutes мин" else ""

        return listOf(hoursPart, minutesPart).filter { it.isNotEmpty() }.joinToString(" ")
    }
}