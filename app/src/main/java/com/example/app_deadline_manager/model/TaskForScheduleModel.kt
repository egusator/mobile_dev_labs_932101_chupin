package com.example.app_deadline_manager.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.compose.schedule.TaskViewForUi
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class TaskForScheduleModel(
    var id: Int,
    var taskId: Int?,
    var periodicTaskId: Int?,
    var workStart: LocalTime,
    var workEnd: LocalTime,
    var date: LocalDate) {
}

