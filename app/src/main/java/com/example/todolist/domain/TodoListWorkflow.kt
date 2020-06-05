package com.example.todolist.domain

import android.util.Log
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.WorkflowAction
import com.squareup.workflow.WorkflowAction.Updater
import java.util.UUID

data class TodoList(
    val title: String,
    val todos: List<Todo> = emptyList()
)

data class Todo(
    val index: Int,
    val id: UUID = UUID.randomUUID(),
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
    class CheckboxTapped(val index: Int) : TodoAction()
    class DescriptionEdited(val index: Int, val text: String) : TodoAction()
    object TodoAdded : TodoAction()

    override fun Updater<TodoList, Nothing>.apply() {
        nextState = when (this@TodoAction) {
            is CheckboxTapped -> {
                nextState.updateRow(index) { copy(completed = !completed) }
            }
            is DescriptionEdited -> {
                nextState.updateRow(index) { copy(description = text) }
            }
            is TodoAdded -> {
                val todo = Todo(nextState.todos.size)
                val updatedTodos = nextState.todos.toMutableList().apply { add(todo) }
                nextState.copy(todos = updatedTodos)
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
                    TodoAction.CheckboxTapped(it)
                )
            },
            todoEdited = { index, text ->
                context.actionSink.send(
                    TodoAction.DescriptionEdited(
                        index,
                        text
                    )
                )
            },
            todoAdded = {
                context.actionSink.send(
                    TodoAction.TodoAdded
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

fun TodoList.updateRow(
    index: Int,
    block: Todo.() -> Todo
): TodoList {
    return copy(
        todos = todos.map { todo ->
            if (todo.index == index) todo.block() else todo
        }
    )
}