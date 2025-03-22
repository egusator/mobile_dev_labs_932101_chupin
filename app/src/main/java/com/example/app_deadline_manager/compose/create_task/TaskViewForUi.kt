package com.example.app_deadline_manager.compose.create_task

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.model.TaskViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class TaskViewForUi(
    var taskName: String = "",
    var description: String = "",
    var deadline: LocalDate = LocalDate.now(),
    var priority: String = "",
    var timeEstimate: String = "",
    var timeSpent: String = "",
    var timeLeft: String = ""
) {
    constructor(taskViewModel: TaskViewModel) : this(
        taskName = taskViewModel.taskName,
        description = taskViewModel.description,
        deadline = taskViewModel.deadline,
        priority = taskViewModel.priority,
        timeEstimate = taskViewModel.timeEstimate(),
        timeSpent = formatTime(taskViewModel.spentTime),
        timeLeft = calculateTimeLeft(taskViewModel)
    )

    companion object {
        fun formatTime(minutes: Int): String {
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            return buildString {
                if (hours > 0) append("$hours ч ")
                if (remainingMinutes > 0) append("$remainingMinutes мин")
            }.trim()
        }

        fun calculateTimeLeft(taskViewModel: TaskViewModel): String {
            val totalEstimatedMinutes = taskViewModel.hours * 60 + taskViewModel.minutes
            val timeLeftMinutes = totalEstimatedMinutes - taskViewModel.spentTime
            return formatTime(timeLeftMinutes)
        }
    }
}