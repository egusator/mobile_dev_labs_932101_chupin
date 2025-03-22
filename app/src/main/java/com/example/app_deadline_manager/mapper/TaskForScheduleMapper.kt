package com.example.app_deadline_manager.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.compose.schedule.TaskViewForUi
import com.example.app_deadline_manager.model.TaskForScheduleModel
import com.example.app_deadline_manager.repository.TaskRepository
import com.example.app_deadline_manager.repository.PeriodicTaskRepository
import java.time.format.DateTimeFormatter

class TaskForScheduleMapper(
    private val taskRepository: TaskRepository,
    private val periodicTaskRepository: PeriodicTaskRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapToTaskView(taskForSchedule: TaskForScheduleModel): TaskViewForUi {
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val taskName = when {
            taskForSchedule.taskId != null -> taskRepository.findById(taskForSchedule.taskId!!)?.name ?: "Задача №${taskForSchedule.id}"
            taskForSchedule.periodicTaskId != null -> periodicTaskRepository.findById(taskForSchedule.periodicTaskId!!)?.name ?: "Задача №${taskForSchedule.id}"
            else -> "Задача №${taskForSchedule.id}"
        }

        return TaskViewForUi(
            taskName = taskName,
            workStart = "Начало работы: ${taskForSchedule.workStart.format(timeFormatter)}",
            workEnd = "Конец работы: ${taskForSchedule.workEnd.format(timeFormatter)}"
        )
    }
}