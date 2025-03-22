package com.example.app_deadline_manager.compose.schedule

import com.example.app_deadline_manager.model.TaskForScheduleModel

data class TaskViewForUi(
    val taskName: String,
    val workStart: String,
    val workEnd: String
)

