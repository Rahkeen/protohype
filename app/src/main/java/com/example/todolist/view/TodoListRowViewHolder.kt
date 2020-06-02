package com.example.todolist.view

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworkflow.R
import com.example.todolist.domain.Todo

class TodoListRowViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val description = view.findViewById<EditText>(R.id.todolist_row_description_layout)
    private val checkbox = view.findViewById<CheckBox>(R.id.todolist_row_checkbox)

    var editAction: (Int, String) -> Unit = {_,_ -> }

    fun bind(
        data: Todo,
        completeAction: (Int) -> Unit
    ) {
        description.setText(data.description)
        description.isEnabled = !data.completed
        description.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {
                editAction(data.index, s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit

        })

        checkbox.isChecked = data.completed
        checkbox.setOnClickListener { completeAction(data.index) }
    }
}