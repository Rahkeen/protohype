package com.example.todolist

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow

data class TodoList(
    val title: String,
    val todos: List<Todo>
)

data class Todo(
    val description: String,
    val completed: Boolean = false
)

object TodoListWorkflow : StatefulWorkflow<Unit, TodoList, Nothing, TodoList>() {
    override fun initialState(props: Unit, snapshot: Snapshot?): TodoList {
        return TodoList(
            title = "Groceries",
            todos = listOf(
                Todo("Apples"),
                Todo("Oranges")
            )
        )
    }

    override fun render(props: Unit, state: TodoList, context: RenderContext<TodoList, Nothing>): TodoList {
        return state
    }

    override fun snapshotState(state: TodoList): Snapshot {
        return Snapshot.EMPTY
    }
}