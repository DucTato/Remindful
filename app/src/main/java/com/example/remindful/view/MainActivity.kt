package com.example.remindful.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.remindful.databinding.ActivityMainBinding
import com.example.remindful.databinding.FragmentAddTaskBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = FragmentAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}