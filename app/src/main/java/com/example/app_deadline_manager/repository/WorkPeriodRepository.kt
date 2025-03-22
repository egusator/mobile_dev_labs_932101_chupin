package com.example.app_deadline_manager.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.compose.create_periodic.WorkPeriod
import com.example.app_deadline_manager.model.WorkPeriodModel
import java.time.DayOfWeek
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class WorkPeriodRepository {

    private val workPeriods: MutableList<WorkPeriodModel> = mutableListOf(
        WorkPeriodModel(1, DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
        WorkPeriodModel(2, DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
        WorkPeriodModel(3, DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
        WorkPeriodModel(4, DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
        WorkPeriodModel(5, DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(17, 0)),
        WorkPeriodModel(6, DayOfWeek.MONDAY, LocalTime.of(18, 0), LocalTime.of(20, 0)),
        WorkPeriodModel(7, DayOfWeek.THURSDAY, LocalTime.of(18, 0), LocalTime.of(20, 0)),
        WorkPeriodModel(8, DayOfWeek.SATURDAY, LocalTime.of(12, 0), LocalTime.of(14, 0))
    )

    fun findAll(): List<WorkPeriodModel> {
        return workPeriods
    }

    fun findById(id: Int): WorkPeriodModel? {
        return workPeriods.find { it.id == id }
    }

    fun addWorkPeriod(workPeriod: WorkPeriodModel) {
        workPeriods.add(workPeriod)
    }

    fun removeWorkPeriod(id: Int): Boolean {
        return workPeriods.removeIf { it.id == id }
    }
}