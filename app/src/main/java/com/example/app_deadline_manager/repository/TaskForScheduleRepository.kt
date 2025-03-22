package com.example.app_deadline_manager.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.model.TaskForScheduleModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class TaskForScheduleRepository {

    private val tasksForSchedule: MutableList<TaskForScheduleModel> = mutableListOf()

    fun getNextId(): Int {
        return (findAll().maxOfOrNull { it.id ?: 0 } ?: 0) + 1
    }
    fun findAll(): List<TaskForScheduleModel> {
        return tasksForSchedule
    }

    fun findById(id: Int): TaskForScheduleModel? {
        return tasksForSchedule.find { it.id == id }
    }

    fun findByTaskId(taskId: Int): List<TaskForScheduleModel> {
        return tasksForSchedule.filter { it.taskId == taskId }
    }

    fun findByPeriodicTaskId(periodicTaskId: Int): List<TaskForScheduleModel> {
        return tasksForSchedule.filter { it.periodicTaskId == periodicTaskId }
    }

    fun findByDate(date: LocalDate): List<TaskForScheduleModel> {
        return tasksForSchedule.filter { it.date == date }
    }

    fun addTaskForSchedule(taskForSchedule: TaskForScheduleModel) {
        tasksForSchedule.add(taskForSchedule)
    }

    fun removeTaskForSchedule(id: Int?): Boolean {
        return tasksForSchedule.removeIf { it.id == id }
    }

    fun saveAll(tasks: List<TaskForScheduleModel>) {
        tasksForSchedule.addAll(tasks)
    }
}