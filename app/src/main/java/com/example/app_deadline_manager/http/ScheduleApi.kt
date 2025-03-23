package com.example.app_deadline_manager.http

import com.example.app_deadline_manager.model.TaskForScheduleModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate

interface ScheduleApi {

    @GET("schedule")
    suspend fun getTasksByDate(@Query("date") date: String): Response<List<TaskForScheduleModel>>

    @POST("schedule")
    suspend fun addTask(@Body task: TaskForScheduleModel): Response<TaskForScheduleModel>
}