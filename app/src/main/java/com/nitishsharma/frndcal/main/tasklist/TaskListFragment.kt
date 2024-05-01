package com.nitishsharma.frndcal.main.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nitishsharma.frndcal.databinding.FragmentTaskListBinding
import com.nitishsharma.frndcal.main.tasklist.clickcallback.TaskListItemClickCallback
import com.nitishsharma.frndcal.main.tasklist.epoxy.TaskListController
import com.nitishsharma.frndcal.main.utils.EventObserver
import com.nitishsharma.frndcal.main.utils.SwipeRefreshCallback
import com.nitishsharma.frndcal.main.utils.setupSwipeRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment(), SwipeRefreshCallback {
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskListViewModel by viewModels()

    private val taskListController: TaskListController by lazy {
        val callback = TaskListItemClickCallback(viewModel)
        TaskListController(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTaskListBinding.inflate(layoutInflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObservers()
    }

    private fun initObservers() {
        viewModel.taskList.observe(viewLifecycleOwner) {
            taskListController.setData(it)
        }
        viewModel.taskDeleted.observe(viewLifecycleOwner, EventObserver {
            if (it)
                Snackbar.make(binding.root, "Task Deleted!", Snackbar.LENGTH_SHORT).show()
            else Snackbar.make(binding.root, "Some Error Occurred!", Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun initRecyclerView() {
        binding.swipeRefresh.setupSwipeRefresh(this)
        taskListController.setFilterDuplicates(true)
        binding.recyclerTaskList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskListController.adapter
        }
    }

    override fun onForcedRefresh() {
        viewModel.getTasks()
    }
}