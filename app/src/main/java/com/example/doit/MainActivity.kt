package com.example.doit

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.doit.Adapter.ToDoAdapter
import com.example.doit.Model.ToDoModel
import com.example.doit.R.id.fab
import com.example.doit.Utils.Databasehandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), DialogCloseListener{
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter

     private var taskList: MutableList<ToDoModel> = mutableListOf()
    private lateinit var db: Databasehandler
    private lateinit var fab:FloatingActionButton


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        supportActionBar?.hide()
//        db = Databasehandler(this)
//        db.openDatabase()
//
//        taskList = db.getAllTasks().toMutableList()
//        taskList.reverse()
//
//
//        tasksRecyclerView = findViewById(R.id.recyclerview)
//        tasksRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        tasksAdapter = ToDoAdapter(db,this,taskList)
//        tasksRecyclerView.adapter = tasksAdapter
//
//        fab=findViewById(R.id.fab)
//
//
//        tasksAdapter.setTasks(taskList)
//        fab.setOnClickListener {
//            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
//        }
//
//
//
//    }
//    override fun handleDialogClose(dialog: DialogInterface) {
//        taskList = db.getAllTasks().toMutableList()
//        taskList.reverse()
//
//        tasksAdapter.setTasks(taskList)
//        tasksAdapter.notifyDataSetChanged()
//    }
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.hide()

    db = Databasehandler(this)
    db.openDatabase()

    tasksRecyclerView = findViewById(R.id.recyclerview)
    tasksRecyclerView.layoutManager = LinearLayoutManager(this)

    taskList = db.getAllTasks().toMutableList().apply { reverse() }
    Log.d("MainActivity", "Task List Size: ${taskList.size}")


    tasksAdapter = ToDoAdapter(db, this, taskList)
    tasksRecyclerView.adapter = tasksAdapter

    fab = findViewById(R.id.fab)
    fab.setOnClickListener {
        AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
    }
    val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
    itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

}

    override fun handleDialogClose(dialog: DialogInterface) {
        taskList = db.getAllTasks().toMutableList().apply { reverse() }
        tasksAdapter.setTasks(taskList)
        tasksAdapter.notifyDataSetChanged()
    }

}