package com.example.doit.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.doit.AddNewTask
import com.example.doit.MainActivity
import com.example.doit.Model.ToDoModel
import com.example.doit.R
import com.example.doit.Utils.Databasehandler

class ToDoAdapter(private val db: Databasehandler,private val activity: MainActivity, private var todoList: List<ToDoModel>) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Implement onBindViewHolder logic here if needed
        db.openDatabase()
        val item: ToDoModel = todoList[position]

        holder.task.text = item.task
        holder.task.isChecked = toBoolean(item.status)
        holder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            val itemId = todoList[holder.adapterPosition].id

            if (isChecked) {
                db.updateStatus(itemId, status = 1)
            } else {
                db.updateStatus(itemId, status = 0)
            }
        }

    }

    private fun toBoolean(n: Int): Boolean {
                return n!=0
    }
    fun getContext(): Context? {
        return activity
    }

    fun deleteItem(position: Int) {
        val item = todoList[position]

        db.deleteTask(item.id)

        val mutableList = todoList.toMutableList()
        mutableList.removeAt(position)

        todoList = mutableList
        notifyItemRemoved(position)
    }


    fun setTasks(todoList: List<ToDoModel>) {
        this.todoList =todoList
        notifyDataSetChanged()
    }
    fun editItem(position: Int) {
        val item = todoList[position]

        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task)

        val fragment = AddNewTask.newInstance()
        fragment.arguments = bundle

        fragment.show(activity.supportFragmentManager, AddNewTask.TAG)
    }


    override fun getItemCount(): Int {
        return todoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder implementation if needed
        var task: CheckBox = itemView.findViewById(R.id.checkbox)

    }
}
