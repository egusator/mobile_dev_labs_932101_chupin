package com.example.app_deadline_manager.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePickerDefaults.dateFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.app_deadline_manager.http.RetrofitInstance.api
import com.example.app_deadline_manager.model.TaskForScheduleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TaskForScheduleRepository {

    private val tasksForSchedule: MutableList<TaskForScheduleModel> = mutableListOf()

    fun findAll(): List<TaskForScheduleModel> {
        return tasksForSchedule
    }

    @OptIn(ExperimentalMaterial3Api::class)
    suspend fun findByDate(date: LocalDate): List<TaskForScheduleModel> {
        return withContext(Dispatchers.IO) {
            val response = api.getTasksByDate(date.toString())
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        }
    }

    suspend fun addTaskForSchedule(taskForSchedule: TaskForScheduleModel): Boolean {
        return withContext(Dispatchers.IO) {
            val response = api.addTask(taskForSchedule)
            response.isSuccessful
        }
    }

    fun removeTaskForSchedule(id: Int?): Boolean {
        return tasksForSchedule.removeIf { it.id == id }
    }
}