package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import com.squareup.workflow.ui.ViewBinding

class TodoListLayoutRunner(view: View): LayoutRunner<TodoListRendering> {

    private val title = view.findViewById<TextView>(R.id.todolist_title)
    private val content = view.findViewById<RecyclerView>(R.id.todolist_content)

    private val todoListAdapter = TodoListContentAdapter(TodoListRendering.empty())

    init {
        content.adapter = todoListAdapter
        content.layoutManager = LinearLayoutManager(view.context)
    }

    override fun showRendering(rendering: TodoListRendering, containerHints: ContainerHints) {
        title.text = rendering.list.title
        todoListAdapter.updateRendering(rendering)
    }

    companion object : ViewBinding<TodoListRendering> by bind(
        R.layout.todolist_layout, ::TodoListLayoutRunner
    )
}

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
        holder.bind(items[position], rendering.todoCompleted)
    }

    fun updateRendering(newRendering: TodoListRendering) {
        rendering = newRendering
        notifyDataSetChanged()
    }
}

class TodoListRowViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<TextView>(R.id.todolist_row_description)
    private val checkbox = view.findViewById<CheckBox>(R.id.todolist_row_checkbox)

    fun bind(data: Todo, action: (Int) -> Unit) {
        description.text = data.description
        checkbox.isChecked = data.completed
        if (data.completed) {
            description.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray))
        } else {
            description.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
        }
        checkbox.setOnCheckedChangeListener { _, _ ->
            action(data.index)
        }
    }
}