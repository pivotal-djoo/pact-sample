package com.example.todo.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.todo.BuildConfig
import com.example.todo.models.ToDo
import com.example.todo.repositories.ToDoRepository
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableToDos = MutableLiveData<List<ToDo>>()
    private val repository = ToDoRepository(BuildConfig.API_URL)

    val currentToDos: LiveData<List<ToDo>?> =
        Transformations.map(mutableToDos) { toDos ->
            toDos
        }

    fun getToDos() {
        viewModelScope.launch {
            mutableToDos.value = repository.getToDos()
        }
    }

    fun addToDo(text: String) {
        viewModelScope.launch {
            val newToDo = ToDo(null, text, null)
            repository.addToDo(newToDo)
            mutableToDos.value = repository.getToDos()
        }
    }

    fun updateToDo(toDo: ToDo) {
        toDo.checked = !(toDo.checked ?: false)
        toDo.id?.let {
            viewModelScope.launch {
                repository.updateToDo(toDo)
                mutableToDos.value = repository.getToDos()
            }
        }
    }

    fun deleteToDo(toDo: ToDo) {
        toDo.id?.let {
            viewModelScope.launch {
                repository.deleteToDo(it)
                mutableToDos.value = repository.getToDos()
            }
        }
    }
}
