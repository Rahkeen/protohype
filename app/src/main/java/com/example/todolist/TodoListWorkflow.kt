package com.example.todolist

import android.util.Log
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.WorkflowAction

data class TodoList(
    val title: String,
    val todos: List<Todo> = emptyList()
)

data class Todo(
    val index: Int,
    val description: String,
    val completed: Boolean = false
)

data class TodoListRendering(
    val list: TodoList,
    val todoCompleted: (index: Int) -> Unit
) {
    companion object {
        fun empty() : TodoListRendering {
            return TodoListRendering(
                list = TodoList(
                    title = "Title",
                    todos = emptyList()
                ),
                todoCompleted = {}
            )
        }
    }
}

sealed class TodoAction: WorkflowAction<TodoList, Nothing> {

    override fun WorkflowAction.Updater<TodoList, Nothing>.apply() {
        when (this@TodoAction) {
            is DoneClicked -> {
                nextState = currentList.copy(
                    todos = currentList.todos.mapIndexed { idx, todo ->
                        if (idx == index) todo.copy(completed = !todo.completed) else todo
                    }
                )
            }
        }
    }

    class DoneClicked(val index: Int, val currentList: TodoList): TodoAction()
}

object TodoListWorkflow : StatefulWorkflow<Unit, TodoList, Nothing, TodoListRendering>() {
    override fun initialState(props: Unit, snapshot: Snapshot?): TodoList {
        return TodoList(
            title = "Groceries",
            todos = listOf(
                Todo(0, "Apples"),
                Todo(1, "Oranges")
            )
        )
    }

    override fun render(props: Unit, state: TodoList, context: RenderContext<TodoList, Nothing>): TodoListRendering {
        return TodoListRendering(
            list = state,
            todoCompleted = { context.actionSink.send(TodoAction.DoneClicked(it, state)) }
        ).also {
            Log.d("TodoListWorkflow", it.toString())
        }
    }

    override fun snapshotState(state: TodoList): Snapshot {
        return Snapshot.EMPTY
    }
}