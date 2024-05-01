package com.example.doit.Utils


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.doit.Model.ToDoModel

class Databasehandler (context: Context) : SQLiteOpenHelper(context, NAME, null, VERSION){

    companion object {
        private const val VERSION = 1
        private const val NAME = "toDoListDatabase"
        private const val TODO_TABLE = "todo"
        private const val ID = "id"
        private const val TASK = "task"
        private const val STATUS = "status"


    }
    private val CREATE_TODO_TABLE =
        "CREATE TABLE $TODO_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TASK TEXT, $STATUS INTEGER)"

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades, if needed
        db.execSQL("DROP TABLE IF EXISTS $TODO_TABLE")

        // Create the table again
        onCreate(db)
    }
    private var db: SQLiteDatabase? = null

    fun openDatabase() {
        db = this.writableDatabase
    }

    fun insertTask(task: ToDoModel) {
        val cv = ContentValues()

        cv.put(TASK, task.task)
        cv.put(STATUS, 0)

        db?.insert(TODO_TABLE, null, cv)
    }
    fun getAllTasks(): List<ToDoModel> {
        val taskList = mutableListOf<ToDoModel>()
        var cur: Cursor? = null

        db?.beginTransaction()

        try {
            cur = db?.query(
                TODO_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
            )

            if (cur != null) {
                if (cur != null &&cur.moveToFirst()) {
                    val idIndex = cur.getColumnIndex(ID)
                    val taskIndex = cur.getColumnIndex(TASK)
                    val statusIndex = cur.getColumnIndex(STATUS)

                    do {
                        val task = ToDoModel().apply {

                            id = cur.getInt(idIndex)
                            task = cur.getString(taskIndex)
                            status = cur.getInt(statusIndex)
                        }
                        taskList.add(task)
                    } while (cur.moveToNext())
                }
            }
        } finally {
            db?.endTransaction()
            cur?.close()
        }

        return taskList
    }
    fun updateStatus(id: Int, status: Int) {
        val cv = ContentValues()

        cv.put(STATUS, status)

        db?.update(TODO_TABLE, cv, "$ID=?", arrayOf(id.toString()))
    }

    fun updateTask(id: Int, task: String) {
        val cv = ContentValues()

        cv.put(TASK, task)

        db?.update(TODO_TABLE, cv, "$ID=?", arrayOf(id.toString()))
    }

    fun deleteTask(id: Int) {
        db?.delete(TODO_TABLE, "$ID=?", arrayOf(id.toString()))
    }

}