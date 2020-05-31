package com.example.todolist.view

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo

class TodoListRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<TextView>(R.id.todolist_row_description)
    private val checkbox = view.findViewById<CheckBox>(R.id.todolist_row_checkbox)

    fun bind(data: Todo, action: (Int) -> Unit) {
        description.text = data.description
        checkbox.isChecked = data.completed
        if (data.completed) {
            description.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.gray
                )
            )
        } else {
            description.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.black
                )
            )
        }
        checkbox.setOnCheckedChangeListener { _, _ ->
            action(data.index)
        }
    }
}