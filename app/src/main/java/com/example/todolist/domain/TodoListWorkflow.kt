package com.example.todolist.domain

import android.util.Log
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
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
    val action: (TodoAction) -> Unit
) {
    companion object {
        fun empty(): TodoListRendering {
            return TodoListRendering(
                list = TodoList(
                    title = "Title",
                    todos = emptyList()
                ),
                action = {}
            )
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
            action = {
                context.actionSink.send(it)
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