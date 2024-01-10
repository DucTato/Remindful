package com.example.remindful.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.remindful.extensions.text
import com.example.remindful.R
import com.example.remindful.aggregation.DataSource
import com.example.remindful.databinding.FragmentAddTaskBinding
import com.example.remindful.model.Task
import com.example.remindful.viewmodel.AddTaskViewModel

class AddTaskFragment : Fragment() {
    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddTaskViewModel
    private var taskId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // check if this action comes with an argument or not
        arguments?.let {
            taskId = it.getString("taskId")!!.toInt()
        }
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddTaskBinding.bind(view)
        binding.apply {
            tilDate.editText?.setOnClickListener {
                // create new instance of DatePickerFragment
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        tilDate.editText?.setText(date)

                    }
                }
                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
            tilTimer.editText?.setOnClickListener(){
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager
                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val time = bundle.getString("SELECTED_TIME")
                        tilTimer.editText?.setText(time)
                    }
                }
                // show
                timePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }
        // return to previous navigation
        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        // set listener for the create task button
        binding.buttonNewTask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                hour = binding.tilTimer.text,
                date = binding.tilDate.text,
                id = taskId
            )
            DataSource.insertTask(task)
            // return to previous screen (fragment)
            findNavController().navigateUp()
        }
        // set default values in case the user is editing
        // an existing record
        if (taskId != 0) {
            // Set up text from the previously selected task
            DataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilTimer.text = it.hour
            }
            // set up new prompt for the button
            binding.buttonNewTask.setText(R.string.update_tasks)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}