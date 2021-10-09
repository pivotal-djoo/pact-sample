package com.example.todo.repositories

import com.example.todo.models.ToDo
import retrofit2.http.*

interface ToDoApiService {
    @GET("todos")
    suspend fun getToDos(): List<ToDo>

    @POST("todos")
    suspend fun addToDo(@Body todo: ToDo): ToDo

    @PUT("todos")
    suspend fun updateToDo(@Body todo: ToDo): ToDo

    @DELETE("todos/{id}")
    suspend fun deleteToDo(@Path("id") id: Long)
}
