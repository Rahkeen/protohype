package com.example.todolist.view

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo
import com.google.android.material.textfield.TextInputLayout

class TodoListRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<TextInputLayout>(R.id.todolist_row_description_layout)
    private val checkbox = view.findViewById<CheckBox>(R.id.todolist_row_checkbox)

    fun bind(data: Todo, action: (Int) -> Unit) {
        description.editText?.setText(data.description)
        checkbox.isChecked = data.completed
        description.isEnabled = !data.completed
        checkbox.setOnCheckedChangeListener { _, _ ->
            action(data.index)
        }
    }
}