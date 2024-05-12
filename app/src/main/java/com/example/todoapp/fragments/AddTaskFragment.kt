package com.example.todoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddTaskBinding
import com.example.todoapp.model.Task
import com.example.todoapp.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Date


class AddTaskFragment : Fragment(R.layout.fragment_add_task), MenuProvider {

    private var addTaskBinding: FragmentAddTaskBinding? = null
    private val binding get() = addTaskBinding!!

    private lateinit var taskViewModel : TaskViewModel
    private lateinit var addTaskView : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addTaskBinding = FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        addTaskView = view

    }

    private fun saveTask(view: View){
        val taskTitle = binding.addTaskTitle.text.toString().trim()
        val taskDesc = binding.addTaskDesc.text.toString().trim()
        val taskDate = SimpleDateFormat("yyyy.MM.dd").format(Date())

        if(taskTitle.isNotEmpty()){
            val task = Task(0,taskTitle,taskDesc,taskDate)
            taskViewModel.addTask(task)

            Toast.makeText(addTaskView.context, "Task Saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }else{
            Toast.makeText(addTaskView.context, "Please enter task title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_task,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu ->{
                saveTask(addTaskView)
                true
            }else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addTaskBinding = null
    }


}