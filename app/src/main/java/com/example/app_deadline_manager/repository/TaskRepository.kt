package com.example.app_deadline_manager.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.model.TaskModel
import com.example.app_deadline_manager.model.TaskPriority
import java.time.LocalDate
@RequiresApi(Build.VERSION_CODES.O)
class TaskRepository {

    private val tasks: MutableList<TaskModel> = mutableListOf(
        TaskModel(1, "1", "ничего интересного", LocalDate.of(2025, 4, 10), TaskPriority.HIGH, 2, 0),
        TaskModel(2, "2", "ничего интересного", LocalDate.of(2025, 4, 12), TaskPriority.MEDIUM, 2, 0),
        TaskModel(3, "3", "ничего интересного", LocalDate.of(2025, 4, 11), TaskPriority.LOW, 3, 30),
        TaskModel(4, "4", "ничего интересного", LocalDate.of(2025, 4, 9), TaskPriority.HIGH, 2, 0),
        TaskModel(5, "5", "ничего интересного", LocalDate.of(2025, 4, 13), TaskPriority.MEDIUM, 3, 0),
        TaskModel(6, "6", "ничего интересного", LocalDate.of(2025, 4, 8), TaskPriority.LOW, 2, 40),
        TaskModel(7, "7", "ничего интересного", LocalDate.of(2025, 4, 15), TaskPriority.HIGH, 2, 0),
        TaskModel(8, "8", "ничего интересного", LocalDate.of(2025, 4, 10), TaskPriority.MEDIUM, 3, 0),
        TaskModel(9, "9", "ничего интересного", LocalDate.of(2025, 4, 14), TaskPriority.LOW, 2, 0),
        TaskModel(10, "10", "ничего интересного", LocalDate.of(2025, 4, 20), TaskPriority.MEDIUM, 2, 20)
    )

    fun findAll(): List<TaskModel> {
        return tasks
    }

    fun findById(id: Int): TaskModel? {
        return tasks.find { it.id == id }
    }
}