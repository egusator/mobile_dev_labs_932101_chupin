package com.example.app_deadline_manager.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class WorkPeriodModel(
    var id: Int,
    var dayOfWeek: DayOfWeek,
    var startTime: LocalTime,
    var endTime: LocalTime
)