package com.example.todolist.domain

import com.squareup.workflow.WorkflowAction

sealed class TodoAction :
    WorkflowAction<TodoList, Nothing> {
    class CheckboxTapped(val index: Int) : TodoAction()
    class DescriptionEdited(val index: Int, val text: String) : TodoAction()
    object TodoAdded : TodoAction()

    override fun WorkflowAction.Updater<TodoList, Nothing>.apply() {
        nextState = when (this@TodoAction) {
            is CheckboxTapped -> {
                nextState.updateRow(index) { copy(completed = !completed) }
            }
            is DescriptionEdited -> {
                nextState.updateRow(index) { copy(description = text) }
            }
            is TodoAdded -> {
                nextState.copy(
                    todos = nextState.todos
                        .toMutableList()
                        .apply { add(Todo(nextState.todos.size)) }
                )
            }
        }
    }
}