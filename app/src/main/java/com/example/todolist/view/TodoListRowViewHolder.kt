package com.example.todolist.view

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo
import com.example.todolist.domain.TodoAction
import com.example.todolist.domain.TodoAction.CheckboxTapped
import com.example.todolist.domain.TodoAction.DescriptionEdited
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TodoListRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<EditText>(R.id.todolist_row_description_layout)
    private val checkbox = view.findViewById<CheckBox>(R.id.todolist_row_checkbox)

    private val textWatcher = ObservableTextWatcher()
    private val disposables = CompositeDisposable()

    fun bind(
        data: Todo,
        action: (TodoAction) -> Unit
    ) {
        bindDescription(data, action)
        bindCheckbox(data, action)
    }

    private fun bindDescription(data: Todo, action: (TodoAction) -> Unit) {
        disposables.clear()
        description.removeTextChangedListener(textWatcher)
        description.setText(data.description)
        description.setSelection(data.description.length)
        description.addTextChangedListener(textWatcher)
        textWatcher.textChanges().subscribe {
            action(DescriptionEdited(data.index, it))
        }.also {
            disposables.add(it)
        }
        description.isEnabled = !data.completed
    }

    private fun bindCheckbox(data: Todo, action: (TodoAction) -> Unit) {
        checkbox.setOnClickListener(null)
        checkbox.isChecked = data.completed
        checkbox.setOnClickListener { action(CheckboxTapped(data.index)) }
    }
}