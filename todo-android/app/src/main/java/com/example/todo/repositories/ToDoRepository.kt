package com.example.todo.repositories

import com.example.todo.models.ToDo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ToDoRepository(baseUrl: String?) {
    private var toDoApiService: ToDoApiService

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        toDoApiService = retrofit.create(ToDoApiService::class.java)
    }

    suspend fun getToDos() = toDoApiService.getToDos()

    suspend fun addToDo(newToDo: ToDo): ToDo {
        return toDoApiService.addToDo(newToDo)
    }

    suspend fun updateToDo(toDo: ToDo): ToDo {
        return toDoApiService.updateToDo(toDo)
    }

    suspend fun deleteToDo(toDoId: Long) {
        toDoApiService.deleteToDo(toDoId)
    }
}
