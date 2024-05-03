package com.nitishsharma.frndcal.main.home.homeNew

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nitishsharma.frndcal.databinding.FragmentHomeFragmentNewBinding
import com.nitishsharma.frndcal.main.home.HomeAddTaskBottomSheet
import com.nitishsharma.frndcal.main.utils.EventObserver
import com.nitishsharma.frndcal.main.utils.navigate
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)

@AndroidEntryPoint
class HomeFragmentNew : Fragment() {
    private lateinit var binding: FragmentHomeFragmentNewBinding
    private val viewModel: HomeFragmentNewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHomeFragmentNewBinding.inflate(layoutInflater, container, false).also {
        binding = it
        binding.viewModel = viewModel
        binding.selectedYear = viewModel.yearLiveData.value
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initObservers()
        setMonthView()
        initGestureListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initGestureListener() {
        val gestureDetector = GestureDetector(context, MyGestureListener())
        binding.calendarRecyclerView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        @Suppress("accidental_override", "nothing_to_override")
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
            val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        previousMonthAction()
                    } else {
                        nextMonthAction()
                    }
                    return true
                }
            }
            return false
        }
    }


    private fun initObservers() {
        viewModel.yearLiveData.observe(viewLifecycleOwner) { year ->
            viewModel.changeSelectedYear(year)
            binding.selectedYear = year
            binding.previousMonthClicked = year < 2021
        }
        viewModel.monthLiveData.observe(viewLifecycleOwner) { month ->
            viewModel.changeSelectedMonth(month)
        }
        viewModel.localDateLiveData.observe(viewLifecycleOwner) { localDate ->
            Timber.e("Date is: $localDate")
            val month = localDate.monthValue
            val year = localDate.year
            Timber.e("Date is: $localDate $month $year")
            setMonthView()
        }
        viewModel.userSelectedDate.observe(viewLifecycleOwner) { userSelectedDate ->
            Timber.e("UserSelectedDate: $userSelectedDate")
        }
        viewModel.taskCreated.observe(viewLifecycleOwner, EventObserver { taskCreated ->
            if (taskCreated) Snackbar.make(binding.root, "Task Added!", Snackbar.LENGTH_SHORT)
                .show()
            else Snackbar.make(binding.root, "Some Error Occurred", Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun initClickListeners() {
        binding.addTaskBtn.setOnClickListener {
            HomeAddTaskBottomSheet().show(childFragmentManager, "HOME_BOTTOM_SHEET")
        }
        binding.viewAllTasks.setOnClickListener {
            val action = HomeFragmentNewDirections.actionHomeFragmentNewToTaskListFragment()
            navigate(action)
        }
    }

    private fun previousMonthAction() {
        if (!(viewModel.localDateLiveData.value?.monthValue == 1 && viewModel.localDateLiveData.value?.year == 2014)) {
            val monthPosition = binding.monthSpinner.selectedItemPosition
            val yearPosition = binding.yearSpinner.selectedItemPosition
            if (monthPosition == 0) {
                binding.selectedYear = viewModel.yearLiveData.value?.minus(1)
                binding.previousMonthClicked = true
                binding.yearSpinner.setSelection(yearPosition + 1)
                binding.monthSpinner.setSelection(11)
            } else {
                binding.monthSpinner.setSelection(monthPosition - 1)
            }
            viewModel.minusMonth()
        }
        Timber.e("Date is ${viewModel.localDateLiveData.value}")
    }

    private fun nextMonthAction() {
        if (!(((viewModel.localDateLiveData.value?.monthValue
                ?: 0) >= viewModel.currentMonth && viewModel.localDateLiveData.value?.year == viewModel.currentYear + 8))
        ) {
            val monthPosition = binding.monthSpinner.selectedItemPosition
            val yearPosition = binding.yearSpinner.selectedItemPosition
            if (monthPosition == 11) {
                binding.previousMonthClicked = false
                binding.monthSpinner.setSelection(0)
                binding.yearSpinner.setSelection(yearPosition - 1)
            } else {
                binding.monthSpinner.setSelection(monthPosition + 1)
            }
            viewModel.plusMonth()
        }
    }

    private fun setMonthView() {
        val daysInMonth: ArrayList<String>? =
            viewModel.localDateLiveData.value?.let {
                daysInMonthArray(
                    it
                )
            }
        val calendarAdapter = daysInMonth?.let { daysOfMonth ->
            viewModel.userSelectedDate.value?.let { selectedDate ->
                viewModel.localDateLiveData.value?.let { localDate ->
                    CalendarAdapter(
                        selectedDate,
                        localDate,
                        daysOfMonth = daysOfMonth,
                        object : CalendarAdapter.OnItemListener {
                            override fun onItemClick(position: Int, dayText: String?) {
                                try {
                                    val paddedMonth = viewModel.monthLiveData.value.toString().padStart(2, '0')
                                    val paddedDay = dayText?.padStart(2, '0')
                                    val text = "${viewModel.yearLiveData.value}-$paddedMonth-$paddedDay"
                                    viewModel.updateUserSelectedDate(LocalDate.parse(text))
                                } catch (e: Exception) {
                                    Timber.e(e.message.toString())
                                }
                            }
                        }
                    )
                }
            }
        }

        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        val daysInMonthArray: ArrayList<String> = ArrayList()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth =
            viewModel.localDateLiveData.value?.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth?.dayOfWeek?.value ?: 0
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("")
            } else {
                daysInMonthArray.add((i - dayOfWeek).toString())
            }
        }
        return daysInMonthArray
    }
}