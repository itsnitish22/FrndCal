package com.nitishsharma.frndcal.main.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nitishsharma.frndcal.main.application.common.CommonViewModel
import com.nitishsharma.frndcal.main.tasklist.datamodel.AllTasks
import com.nitishsharma.frndcal.main.tasklist.datamodel.DeleteTaskRequestModel
import com.nitishsharma.frndcal.main.tasklist.datamodel.RemoteTask
import com.nitishsharma.frndcal.main.tasklist.datamodel.TaskListRequestModel
import com.nitishsharma.frndcal.main.tasklist.repository.TaskListDefaultRepository
import com.nitishsharma.frndcal.main.utils.Event
import com.nitishsharma.frndcal.main.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    val repository: TaskListDefaultRepository
) : CommonViewModel() {

    private val _taskList: MutableLiveData<AllTasks> = MutableLiveData()
    val taskList: LiveData<AllTasks> get() = _taskList

    private val _taskDeleted: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val taskDeleted: LiveData<Event<Boolean>> get() = _taskDeleted

    init {
        getTasks()
    }

    fun getTasks() {
        viewModelScope.launch {
            val response = repository.getTaskList(TaskListRequestModel(2210))
            if (response is Result.Success) {
                _taskList.postValue(response.data)
            } else if (response is Result.Error) {
                Timber.e("Error fetching tasks: ${response.exception.message}")
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            val response = repository.deleteTask(DeleteTaskRequestModel(2210, taskId))
            if (response is Result.Success) {
                _taskDeleted.postValue(Event(true))
                removeTaskLocally(taskId)
            } else if (response is Result.Error) {
                Timber.e("Error deleting task: ${response.exception.message}")
                _taskDeleted.postValue(Event(false))
            }
        }
    }

    private fun removeTaskLocally(taskIdLocal: Int) {
        val allTasks = taskList.value?.taskList
        allTasks?.let { taskList ->
            val newList = taskList.filterNot { it.taskId == taskIdLocal }
            _taskList.postValue(AllTasks(newList as ArrayList<RemoteTask>))
        }
    }
}