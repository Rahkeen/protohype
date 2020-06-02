package com.example.todolist.domain

import android.util.Log
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.WorkflowAction
import com.squareup.workflow.WorkflowAction.Updater

data class TodoList(
    val title: String,
    val todos: List<Todo> = emptyList()
)

data class Todo(
    val index: Int,
    val description: String = "Untitled",
    val completed: Boolean = false
)

data class TodoListRendering(
    val list: TodoList,
    val todoCompleted: (index: Int) -> Unit,
    val todoEdited: (index: Int, text: String) -> Unit,
    val todoAdded: () -> Unit
) {
    companion object {
        fun empty(): TodoListRendering {
            return TodoListRendering(
                list = TodoList(
                    title = "Title",
                    todos = emptyList()
                ),
                todoCompleted = {},
                todoEdited = { _, _ -> },
                todoAdded = {}
            )
        }
    }
}

sealed class TodoAction : WorkflowAction<TodoList, Nothing> {
    class CheckboxTapped(val index: Int, val list: TodoList) : TodoAction()
    class DescriptionEdited(val index: Int, val text: String, val list: TodoList) : TodoAction()
    class TodoAdded(val list: TodoList) : TodoAction()

    override fun Updater<TodoList, Nothing>.apply() {
        nextState = when (this@TodoAction) {
            is CheckboxTapped -> {
                list.updateRow(index) { copy(completed = !completed) }
            }
            is DescriptionEdited -> {
                list.updateRow(index) { copy(description = text) }
            }
            is TodoAdded -> {
                val todo = Todo(list.todos.size)
                val updatedTodos = list.todos.toMutableList().apply {
                    add(todo)
                }
                list.copy(todos = updatedTodos)
            }
        }
    }
}

object TodoListWorkflow : StatefulWorkflow<Unit, TodoList, Nothing, TodoListRendering>() {

    override fun initialState(props: Unit, snapshot: Snapshot?): TodoList {
        return TodoList(
            title = "Todos"
        )
    }

    override fun render(props: Unit, state: TodoList, context: RenderContext<TodoList, Nothing>): TodoListRendering {
        return TodoListRendering(
            list = state,
            todoCompleted = {
                context.actionSink.send(
                    TodoAction.CheckboxTapped(
                        it,
                        state
                    )
                )
            },
            todoEdited = { index, text ->
                context.actionSink.send(
                    TodoAction.DescriptionEdited(
                        index,
                        text,
                        state
                    )
                )
            },
            todoAdded = {
                context.actionSink.send(
                    TodoAction.TodoAdded(state)
                )
            }
        ).also {
            Log.d("TodoListWorkflow", it.toString())
        }
    }

    override fun snapshotState(state: TodoList): Snapshot {
        return Snapshot.EMPTY
    }
}

private fun TodoList.updateRow(
    index: Int,
    block: Todo.() -> Todo
): TodoList {
    return copy(
        todos = todos.withIndex()
            .map { (i, value) ->
                if (i == index) value.block() else value
            }
    )
}