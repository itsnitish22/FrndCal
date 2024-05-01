package com.nitishsharma.frndcal.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.nitishsharma.frndcal.databinding.FragmentHomeBinding
import com.nitishsharma.frndcal.main.utils.EventObserver
import com.nitishsharma.frndcal.main.utils.navigate
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewmodel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(layoutInflater, container, false).also {
        binding = it
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        viewmodel.selectedMonth.observe(viewLifecycleOwner) {
            updateCalendar(it, viewmodel.selectedYear.value, viewmodel.selectedDate.value)
        }
        viewmodel.selectedYear.observe(viewLifecycleOwner) {
            updateCalendar(viewmodel.selectedMonth.value, it, viewmodel.selectedDate.value)
        }
        viewmodel.selectedDate.observe(viewLifecycleOwner) {
            updateCalendar(viewmodel.selectedMonth.value, viewmodel.selectedYear.value, it)
        }
        viewmodel.taskCreated.observe(viewLifecycleOwner, EventObserver { taskCreated ->
            if (taskCreated) Snackbar.make(binding.root, "Task Added!", Snackbar.LENGTH_SHORT)
                .show()
            else Snackbar.make(binding.root, "Some Error Occurred", Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun updateCalendar(month: Int?, year: Int?, date: Int?) {
        month?.let { selectedMonth ->
            year?.let { selectedYear ->
                val finalYear = viewmodel.yearList.value?.getOrNull(selectedYear)
                finalYear?.let { finalYearString ->
                    date?.let { selectedDate ->
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, finalYearString.toInt())
                            set(Calendar.MONTH, selectedMonth)
                            set(Calendar.DAY_OF_MONTH, selectedDate)
                        }
                        binding.calendarView.date = calendar.timeInMillis
                    }
                }
            }
        }
    }


    private fun initViews() {
        with(binding.calendarView) {
            date = System.currentTimeMillis()
            setOnDateChangeListener { view, year, month, dayOfMonth ->
                Timber.e("Selected Date: $dayOfMonth-${month + 1}-$year")
                updateSelectedDate(year, month, dayOfMonth)
            }
        }
        binding.addTaskBtn.setOnClickListener {
            HomeAddTaskBottomSheet().show(childFragmentManager, "HOME_BOTTOM_SHEET")
        }
        binding.viewAllTasks.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToTaskListFragment()
            navigate(action)
        }
    }

    private fun updateSelectedDate(year: Int, month: Int, dayOfMonth: Int) {
        viewmodel.updateSelectedMonth(month)
        viewmodel.updateSelectedYear(year)
        viewmodel.updateSelectedDate(dayOfMonth)
    }
}