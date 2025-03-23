package com.example.app_deadline_manager.http

import com.example.app_deadline_manager.model.TaskForScheduleModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
interface ScheduleApi {

    // Изменим путь и параметры, чтобы совпадать с Spring API
    @GET("tasks/date/{date}")
    suspend fun getTasksByDate(@Path("date") date: String): Response<List<TaskForScheduleModel>>

    // POST запрос остается без изменений
    @POST("tasks")
    suspend fun addTask(@Body task: TaskForScheduleModel): Response<TaskForScheduleModel>
}