package com.example.app_deadline_manager.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class TaskModel(
    var id: Int,
    var name: String,
    var description: String,
    var deadline: LocalDate = LocalDate.now(),
    var priority: TaskPriority,
    var hours: Int,
    var minutes: Int,
    var spentTime: Int = 0
)