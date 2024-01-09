package com.example.remindful.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.remindful.R
import com.example.remindful.databinding.ActivityMainBinding
import com.example.remindful.databinding.FragmentTaskBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // set up navigation host from this activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        findViewById<Button>(R.id.new_task_button).setOnClickListener {
            // define an action to this listener
            val action = TaskFragmentDirections.actionTaskFragmentToAddTaskFragment()
            navController.navigate(action)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}