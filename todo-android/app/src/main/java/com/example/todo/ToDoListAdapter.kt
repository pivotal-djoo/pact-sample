package com.example.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.models.ToDo

class ToDoListAdapter(private val callback: Callback) :
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {
    private var localDataSet: List<ToDo>? = null

    interface Callback {
        fun deleteTapped(todo: ToDo)
        fun checkboxTapped(todo: ToDo, checked: Boolean)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val deleteImageView: ImageView
        val checkBox: CheckBox

        init {
            textView = view.findViewById(R.id.text_view)
            deleteImageView = view.findViewById(R.id.delete_image)
            checkBox = view.findViewById(R.id.checkbox)
        }
    }

    fun setData(dataSet: List<ToDo>?) {
        localDataSet = dataSet
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.todo_row_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val toDo: ToDo = localDataSet!![position]
        viewHolder.textView.setText(toDo.text)
        viewHolder.deleteImageView
            .setOnClickListener { v: View? ->
                callback.deleteTapped(toDo)
            }
        viewHolder.checkBox.isChecked = toDo.checked ?: false
        viewHolder.checkBox
            .setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                callback.checkboxTapped(toDo, isChecked)
            }
    }

    override fun getItemCount(): Int {
        return localDataSet?.size ?: 0
    }
}