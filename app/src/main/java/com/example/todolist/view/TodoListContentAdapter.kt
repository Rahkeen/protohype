package com.example.todolist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo
import com.example.todolist.domain.TodoListRendering

class TodoListContentAdapter(private var rendering: TodoListRendering) : RecyclerView.Adapter<TodoListRowViewHolder>() {

    private val items: List<Todo>
        get() = rendering.list.todos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListRowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todolist_row_view, parent, false)

        return TodoListRowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TodoListRowViewHolder, position: Int) {
        holder.bind(items[position], rendering.todoCompleted, rendering.todoEdited)
    }

    fun updateRendering(newRendering: TodoListRendering) {
        rendering = newRendering
        notifyDataSetChanged()
    }
}