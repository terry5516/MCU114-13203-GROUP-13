package com.example.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ui.R
import com.example.ui.model.Todo

class TodoAdapter(
    private val todos: List<Todo>
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.cb_todo)
        val title: TextView = view.findViewById(R.id.tv_title)
        val dueDate: TextView = view.findViewById(R.id.tv_due_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = todos[position]
        holder.title.text = item.title
        holder.dueDate.text = item.dueDate
        holder.checkBox.isChecked = item.isCompleted
    }

    override fun getItemCount() = todos.size
}