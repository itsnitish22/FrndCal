package com.nitishsharma.frndcal.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nitishsharma.frndcal.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.Calendar


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeBinding.inflate(layoutInflater, container, false).also {
        binding = it
        binding.viewModel = viewmodel
        binding.lifecycleOwner = viewLifecycleOwner

        initViews()
        initObservers()
    }.root

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
    }

    private fun updateSelectedDate(year: Int, month: Int, dayOfMonth: Int) {
        viewmodel.updateSelectedMonth(month)
        viewmodel.updateSelectedYear(year)
        viewmodel.updateSelectedDate(dayOfMonth)
    }
}