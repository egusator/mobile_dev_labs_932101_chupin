package com.example.app_deadline_manager.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.compose.create_periodic.WorkPeriod
import com.example.app_deadline_manager.model.PeriodicTaskModel
import java.time.DayOfWeek
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class PeriodicTaskRepository {

    private val periodicTasks: MutableList<PeriodicTaskModel> = mutableListOf(
        PeriodicTaskModel(
            1, "работа", "ничего интересного",
            listOf(
                WorkPeriod(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
                WorkPeriod(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
                WorkPeriod(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
                WorkPeriod(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
                WorkPeriod(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(17, 0))
            )
        ),
        PeriodicTaskModel(
            2, "спортзал", "ничего интересного",
            listOf(
                WorkPeriod(DayOfWeek.MONDAY, LocalTime.of(18, 0), LocalTime.of(20, 0)),
                WorkPeriod(DayOfWeek.THURSDAY, LocalTime.of(18, 0), LocalTime.of(20, 0)),
                WorkPeriod(DayOfWeek.SATURDAY, LocalTime.of(12, 0), LocalTime.of(14, 0))
            )
        )
    )

    fun findAll(): List<PeriodicTaskModel> {
        return periodicTasks
    }

    fun findById(id: Int): PeriodicTaskModel? {
        return periodicTasks.find { it.id == id }
    }

    fun addTask(task: PeriodicTaskModel) {
        periodicTasks.add(task)
    }

    fun removeTask(id: Int): Boolean {
        return periodicTasks.removeIf { it.id == id }
    }

    fun findByDayOfWeek(day: DayOfWeek): List<PeriodicTaskModel> {
        return periodicTasks.filter { task ->
            task.workPeriods.any { it.day == day }
        }
    }
}