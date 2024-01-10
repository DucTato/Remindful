package com.example.remindful.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.remindful.R
import com.example.remindful.aggregation.DataSource
import com.example.remindful.databinding.FragmentTaskBinding
import com.example.remindful.model.TaskListAdapter
import com.example.remindful.viewmodel.TaskViewModel

class TaskFragment : Fragment() {

    companion object {
        fun newInstance() = TaskFragment()
    }

    private lateinit var viewModel: TaskViewModel
    // this bool is here to determine the state of the current layout
    private var isLinearLayoutManager: Boolean = false
    // make a reference to the view binding
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    // declare a reference to the recycler view inside this fragment
    private lateinit var recyclerView: RecyclerView
    //
    private val adapter by lazy { TaskListAdapter() }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.taskList
        binding.newTaskButton.setOnClickListener {
            val action = TaskFragmentDirections.actionTaskFragmentToAddTaskFragment()
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        chooseLayout()
        drawList()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        setIcon(layoutButton)

    }
    private fun setIcon(menuItem: MenuItem?){
        if (menuItem == null){
            return
        }
        menuItem.icon =
            if (isLinearLayoutManager) ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }
    private fun chooseLayout(){
        when (isLinearLayoutManager){
            true -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = TaskListAdapter()
            }
            false ->{
                recyclerView.layoutManager = GridLayoutManager(context,2)
                recyclerView.adapter = TaskListAdapter()
            }
        }
        recyclerView.adapter = adapter
    }
    private fun drawList(){
        val list = DataSource.getList()
        if(list.isEmpty()){
            binding.emptyInclude.emptyLayoutScreen.visibility = View.VISIBLE
        }
        else{
            binding.emptyInclude.emptyLayoutScreen.visibility = View.GONE
        }
        adapter.submitList(list)
    }
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Sets isLinearLayoutManager (a Boolean) to the opposite value
                isLinearLayoutManager = !isLinearLayoutManager
                // Sets layout and icon
                chooseLayout()
                setIcon(item)

                return true
            }
            // Otherwise, do nothing and use the core event handling

            // when clauses require that all possible paths be accounted for explicitly,
            // for instance both the true and false cases if the value is a Boolean,
            // or an else to catch all unhandled cases.
            else -> super.onOptionsItemSelected(item)
        }
    }
}