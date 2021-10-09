package com.example.todo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.models.ToDo
import com.example.todo.viewmodels.ToDoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class MainActivity : AppCompatActivity(), ToDoListAdapter.Callback, CoroutineScope by MainScope() {

    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val toDosAdapter = ToDoListAdapter(this)
        recyclerView.adapter = toDosAdapter

        toDoViewModel.currentToDos.observe(this, Observer { toDos ->
            toDosAdapter.setData(toDos)
            toDosAdapter.notifyDataSetChanged()
        })

        toDoViewModel.getToDos()

        val addToDoButton = findViewById<Button>(R.id.add_to_do_button)
        addToDoButton.setOnClickListener { button: View? -> handleAddToDoButtonTapped() }
    }

    override fun deleteTapped(toDo: ToDo) {
        Log.d("MainActivity", "######### delete tapped")
        toDoViewModel.deleteToDo(toDo)
    }

    override fun checkboxTapped(toDo: ToDo, checked: Boolean) {
        Log.d("MainActivity", "######### checkbox checked: $checked")
        toDoViewModel.updateToDo(toDo)
    }

    private fun handleAddToDoButtonTapped() {
        Log.d("MainActivity", "######### add todo tapped")
        val addToDoEditText = findViewById<EditText>(R.id.new_to_do_edittext)
        toDoViewModel.addToDo(addToDoEditText.text.toString())
        addToDoEditText.text.clear()
    }
}
