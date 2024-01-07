package com.example.remindful.model

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.remindful.R

class TaskListAdapter:RecyclerView.Adapter<TaskListAdapter.TaskListHolder>() {
    // provides a reference for the views needed to display its content
    class TaskListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.task_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    override fun onBindViewHolder(holder: TaskListHolder, position: Int) {
        TODO("Not yet implemented")
    }
}