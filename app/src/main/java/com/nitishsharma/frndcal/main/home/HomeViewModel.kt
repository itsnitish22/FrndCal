package com.nitishsharma.frndcal.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nitishsharma.frndcal.main.application.common.CommonViewModel
import com.nitishsharma.frndcal.main.home.datamodel.StoreTaskRequestModel
import com.nitishsharma.frndcal.main.home.datamodel.TaskModel
import com.nitishsharma.frndcal.main.home.repository.HomeFragmentDefaultRepository
import com.nitishsharma.frndcal.main.utils.Event
import com.nitishsharma.frndcal.main.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: HomeFragmentDefaultRepository
) : CommonViewModel() {

    private val YEAR_RANGE = 50

    val monthsArray = MutableLiveData<Array<String>>()
    val yearList = MutableLiveData<List<String>>()
    val selectedMonth = MutableLiveData<Int>(getCurrentMonth())
    val selectedYear = MutableLiveData<Int>(50)
    val selectedDate = MutableLiveData<Int>(getCurrentDate())

    private val _taskCreated: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val taskCreated: LiveData<Event<Boolean>> get() = _taskCreated

    init {
        initMonths()
        initYearList()
        setDefaultSelectedYear()
    }

    private fun setDefaultSelectedYear() {
        viewModelScope.launch {
            delay(1000)
            selectedMonth.value = getCurrentMonth()
            selectedYear.value = yearList.value?.indexOf(getCurrentYear().toString())
        }
    }

    private fun initMonths() {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        monthsArray.value = months
    }

    private fun initYearList() {
        val currentYear = getCurrentYear()
        val years = (currentYear - YEAR_RANGE..currentYear + YEAR_RANGE).map { it.toString() }
        yearList.value = years
    }

    private fun getCurrentYear() = Calendar.getInstance().get(Calendar.YEAR)
    private fun getCurrentMonth() = Calendar.getInstance().get(Calendar.MONTH)
    private fun getCurrentDate() = Calendar.getInstance().get(Calendar.DATE)

    fun updateSelectedYear(year: Int) {
        selectedYear.value = yearList.value?.indexOf(year.toString())
    }

    fun updateSelectedMonth(month: Int) {
        selectedMonth.value = month
    }

    fun updateSelectedDate(date: Int) {
        selectedDate.value = date
    }

    fun onMonthItemSelected(position: Int) {
        selectedMonth.value = position
    }

    fun onYearItemSelected(position: Int) {
        selectedYear.value = position
    }

    fun storeTaskRemote(taskTitle: String, taskDescription: String) {
        viewModelScope.launch {
            val taskRequestModel = StoreTaskRequestModel(
                2210,
                TaskModel(
                    if (taskTitle.isNullOrEmpty()) "Dummy Title" else taskTitle,
                    if (taskDescription.isNullOrEmpty()) "Dummy description" else taskDescription,
                    "${selectedDate.value}-${selectedMonth.value?.plus(1)}-${
                        selectedYear.value?.let {
                            yearList.value?.get(
                                it
                            )
                        }
                    }"
                )
            )
            val response = repository.storeTask(taskRequestModel)
            if (response is Result.Success) {
                _taskCreated.postValue(Event(true))
            } else if (response is Result.Error) {
                Timber.e("Unable to create task: ${response.exception.message}")
                _taskCreated.postValue(Event(false))
            }
        }
    }
}
