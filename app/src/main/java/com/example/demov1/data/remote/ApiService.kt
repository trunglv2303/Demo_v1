package com.example.demov1.data.remote

import com.example.demov1.data.model.Task
import retrofit2.http.GET

interface ApiService {

    @GET("todos")
    suspend fun getTasks(): List<Task>

}

