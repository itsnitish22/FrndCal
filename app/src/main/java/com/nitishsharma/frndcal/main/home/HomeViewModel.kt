package com.nitishsharma.frndcal.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nitishsharma.frndcal.main.application.common.CommonViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class HomeViewModel @Inject constructor() : CommonViewModel() {

    private val YEAR_RANGE = 50

    val monthsArray = MutableLiveData<Array<String>>()
    val yearList = MutableLiveData<List<String>>()
    val selectedMonth = MutableLiveData<Int>(getCurrentMonth())
    val selectedYear = MutableLiveData<Int>(50)
    val selectedDate = MutableLiveData<Int>(getCurrentDate())

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
}
