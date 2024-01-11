package com.example.remindful.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.example.remindful.extensions.text
import com.example.remindful.R
import com.example.remindful.aggregation.DataSource
import com.example.remindful.databinding.FragmentAddTaskBinding
import com.example.remindful.model.AlarmReceiver
import com.example.remindful.model.Task
import com.example.remindful.viewmodel.AddTaskViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class AddTaskFragment : Fragment() {
    companion object {
        fun newInstance() = AddTaskFragment()
    }

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AddTaskViewModel
    private var taskId: Int = 0
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar: Calendar

    val timePickerFragment = TimePickerFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // check if this action comes with an argument or not
        arguments?.let {
            taskId = it.getString("taskId")!!.toInt()
        }
        setHasOptionsMenu(true)
        createNotificationChannel()
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
            tilTimer.editText?.setOnClickListener() {
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
            if (task.title.isEmpty() || task.hour.isEmpty() || task.date.isEmpty()) {
                findNavController().navigateUp()
            } else {
                DataSource.insertTask(task)
                // return to previous screen (fragment)
                findNavController().navigateUp()
                setAlarm(task)
            }
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

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val name: CharSequence = "NotificationChannel"
           val description = "Channel for Alarm Manager"
           val importance = NotificationManager.IMPORTANCE_HIGH
           val channel = NotificationChannel("myNotificationID",name,importance)
           channel.description = description
           val notificationManager = getSystemService(
               this.requireContext(),NotificationManager::class.java
           )
           notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(task: Task) {
        val alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this.requireContext(),AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this.requireContext(),0,intent,
            PendingIntent.FLAG_IMMUTABLE)
        val text = task.hour
        val pattern = DateTimeFormatter.ofPattern("HH:mm")
        val localTime = LocalTime.parse(text, pattern)
        val now = LocalDateTime.now()
        val alarmDateTime = LocalDateTime.of(now.toLocalDate(), localTime)
        val milliseconds = alarmDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (alarmDateTime.isBefore(now)) {
            alarmDateTime.plusDays(1)
        }
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,milliseconds,
            pendingIntent
        )
        Toast.makeText(this.requireContext(),"Alarm set successfully", Toast.LENGTH_SHORT).show()
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