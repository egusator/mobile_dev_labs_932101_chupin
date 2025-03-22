package com.example.app_deadline_manager.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import kotlin.compareTo

@RequiresApi(Build.VERSION_CODES.O)
data class TaskViewModel(
    var taskName: String = "",
    var description: String = "",
    var deadline: LocalDate = LocalDate.now(),
    var priority: String = "",
    var hours: Int = 0,
    var minutes: Int = 0,
    var spentTime: Int = 0, //minutes
) {
    fun timeEstimate(): String {
        val hoursPart = if (hours > 0) "$hours ч" else ""
        val minutesPart = if (minutes > 0) "$minutes мин" else ""

        return listOf(hoursPart, minutesPart).filter { it.isNotEmpty() }.joinToString(" ")
    }
}