package com.example.doit

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.doit.Model.ToDoModel
import com.example.doit.Utils.Databasehandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask : BottomSheetDialogFragment() {
    private lateinit var newTaskText: EditText
    private lateinit var newTaskSaveButton: Button
    private lateinit var db: Databasehandler

    companion object {
        const val TAG = "ActionBottomDialog"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_task, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newTaskText = view.findViewById(R.id.newtask)
        newTaskSaveButton = view.findViewById(R.id.savebtn)

        db = Databasehandler(requireActivity())
        db.openDatabase()
        val bundle = arguments
        var isUpdate = false

        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")

            newTaskText.setText(task)

            if (task?.length ?: 0 > 0) {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            }

        }
        newTaskText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed before text changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No implementation needed after text changed
            }
        })
        val finalIsUpdate = isUpdate

        newTaskSaveButton.setOnClickListener {
            val text = newTaskText.text.toString()

            if (finalIsUpdate) {
                bundle?.let {
                    db.updateTask(it.getInt("id"), text)
                }
            } else {
                val task = ToDoModel()
                task.task = text
                task.status = 0
                db.insertTask(task)
                // Perform any other necessary operations with the new task
            }
            dismiss()
        }
    }
    override fun onDismiss(dialog: DialogInterface) {
        val activity = activity
        if (activity is DialogCloseListener) {
            (activity as DialogCloseListener).handleDialogClose(dialog)
        }
    }

}