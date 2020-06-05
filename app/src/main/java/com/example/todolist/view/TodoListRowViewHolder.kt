package com.example.todolist.view

import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TodoListRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<EditText>(R.id.todolist_row_description_layout)
    private val checkbox = view.findViewById<CheckBox>(R.id.todolist_row_checkbox)

    private val textWatcher = ObservableTextWatcher()
    private val disposables = CompositeDisposable()

    fun bind(
        data: Todo,
        completeAction: (Int) -> Unit,
        editAction: (Int, String) -> Unit
    ) {
        bindDescription(data, editAction)
        bindCheckbox(data, completeAction)
    }

    private fun bindDescription(data: Todo, editAction: (Int, String) -> Unit) {
        disposables.clear()
        description.removeTextChangedListener(textWatcher)
        description.setText(data.description)
        description.setSelection(data.description.length)
        description.addTextChangedListener(textWatcher)
        textWatcher.textChanges().subscribe {
            editAction(data.index, it)
        }.also {
            disposables.add(it)
        }
        description.isEnabled = !data.completed
    }

    private fun bindCheckbox(data: Todo, completeAction: (Int) -> Unit) {
        checkbox.setOnClickListener(null)
        checkbox.isChecked = data.completed
        checkbox.setOnClickListener { completeAction(data.index) }
    }
}