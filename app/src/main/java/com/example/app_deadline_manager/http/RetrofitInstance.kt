package com.example.app_deadline_manager.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://localhost:8080/"

    val api: ScheduleApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // для сериализации объектов в JSON
            .build()
            .create(ScheduleApi::class.java)
    }
}