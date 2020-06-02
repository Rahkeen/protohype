package com.example.todolist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo
import com.example.todolist.domain.TodoListRendering

class TodoListContentAdapter(private var rendering: TodoListRendering) : RecyclerView.Adapter<TodoListRowViewHolder>() {

    private val items: List<Todo>
        get() = rendering.list.todos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListRowViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.todolist_row_view, parent, false)

        return TodoListRowViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TodoListRowViewHolder, position: Int) {
        holder.editAction = {_,_ -> }
        holder.bind(
            items[position],
            rendering.todoCompleted
        )
        holder.editAction = rendering.todoEdited
    }

    fun updateRendering(newRendering: TodoListRendering) {
        val diffCallback = DiffCallback(rendering, newRendering)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        rendering = newRendering
        diffResult.dispatchUpdatesTo(this)
    }

    private class DiffCallback(
        private val oldRendering: TodoListRendering,
        private val newRendering: TodoListRendering
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemPosition == newItemPosition
        }

        override fun getOldListSize(): Int {
            return oldRendering.list.todos.size
        }

        override fun getNewListSize(): Int {
            return newRendering.list.todos.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldRendering.list.todos[oldItemPosition]
            val newItem = newRendering.list.todos[newItemPosition]

            return oldItem == newItem
        }

    }
}