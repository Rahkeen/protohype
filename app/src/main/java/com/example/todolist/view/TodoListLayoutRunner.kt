package com.example.todolist.view

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.TodoListRendering
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import com.squareup.workflow.ui.ViewBinding

class TodoListLayoutRunner(view: View): LayoutRunner<TodoListRendering> {

    private val title = view.findViewById<TextView>(R.id.todolist_title)
    private val content = view.findViewById<RecyclerView>(R.id.todolist_content)
    private val addButton = view.findViewById<FloatingActionButton>(R.id.todolist_add_button)

    private val todoListAdapter = TodoListContentAdapter(TodoListRendering.empty())

    init {
        content.adapter = todoListAdapter
        content.layoutManager = LinearLayoutManager(view.context)
    }

    override fun showRendering(rendering: TodoListRendering, containerHints: ContainerHints) {
        title.text = rendering.list.title
        addButton.setOnClickListener { rendering.todoAdded() }
        todoListAdapter.updateRendering(rendering)
    }

    companion object : ViewBinding<TodoListRendering> by bind(
        R.layout.todolist_layout, ::TodoListLayoutRunner
    )
}

