package com.nitishsharma.frndcal.main.home.homeNew

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nitishsharma.frndcal.main.home.datamodel.StoreTaskRequestModel
import com.nitishsharma.frndcal.main.home.datamodel.TaskModel
import com.nitishsharma.frndcal.main.home.repository.HomeFragmentDefaultRepository
import com.nitishsharma.frndcal.main.utils.Event
import com.nitishsharma.frndcal.main.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)

@HiltViewModel
class HomeFragmentNewViewModel @Inject constructor(
    private val repository: HomeFragmentDefaultRepository
) : ViewModel() {
    private val _currentYear = LocalDate.now().year
    val currentYear get() = _currentYear
    private val _currentMonth = LocalDate.now().monthValue
    val currentMonth get() = _currentMonth

    private val _yearLiveData: MutableLiveData<Int> = MutableLiveData(LocalDate.now().year)
    val yearLiveData: LiveData<Int> get() = _yearLiveData

    private val _monthLiveData: MutableLiveData<Int> = MutableLiveData(LocalDate.now().monthValue)
    val monthLiveData: LiveData<Int> get() = _monthLiveData

    private val _localDateLiveData: MutableLiveData<LocalDate> =
        MutableLiveData(LocalDate.now())
    val localDateLiveData: LiveData<LocalDate> get() = _localDateLiveData

    private val _taskCreated: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val taskCreated: LiveData<Event<Boolean>> get() = _taskCreated

    private val _userSelectedDate: MutableLiveData<LocalDate> =
        MutableLiveData(_localDateLiveData.value)
    val userSelectedDate: LiveData<LocalDate> get() = _userSelectedDate


    fun minusMonth() {
        _localDateLiveData.postValue(_localDateLiveData.value?.minusMonths(1))
    }

    fun plusMonth() {
        _localDateLiveData.postValue(_localDateLiveData.value?.plusMonths(1))
    }

    fun changeSelectedYear(year: Int) {
        _localDateLiveData.postValue(_localDateLiveData.value?.withYear(year))
    }

    fun changeSelectedMonth(month: Int) {
        _localDateLiveData.postValue(_localDateLiveData.value?.withMonth(month))
    }

    fun updateYearLiveData(year: Int) {
        _yearLiveData.postValue(year)
    }

    fun updateMonthLiveData(month: Int) {
        _monthLiveData.postValue(month)
    }

    fun storeTaskRemote(taskTitle: String, taskDescription: String) {
        viewModelScope.launch {
            val taskRequestModel = StoreTaskRequestModel(
                2210,
                TaskModel(
                    if (taskTitle.isNullOrEmpty()) "Dummy Title" else taskTitle,
                    if (taskDescription.isNullOrEmpty()) "Dummy description" else taskDescription,
                    taskDate = _userSelectedDate.value.toString()
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

    fun updateUserSelectedDate(userDate: LocalDate) {
        _userSelectedDate.postValue(userDate)
    }
}