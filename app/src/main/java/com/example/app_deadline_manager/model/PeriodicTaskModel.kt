package com.example.app_deadline_manager.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.compose.create_periodic.WorkPeriod

@RequiresApi(Build.VERSION_CODES.O)
data class PeriodicTaskModel(
    var id: Int,
    var name: String,
    var description: String,
    var workPeriods: List<WorkPeriod>
)