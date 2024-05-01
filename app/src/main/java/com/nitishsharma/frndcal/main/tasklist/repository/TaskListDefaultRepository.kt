package com.nitishsharma.frndcal.main.tasklist.repository

import com.nitishsharma.frndcal.main.tasklist.datamodel.AllTasks
import com.nitishsharma.frndcal.main.tasklist.datamodel.DeleteTaskRequestModel
import com.nitishsharma.frndcal.main.tasklist.datamodel.TaskListRequestModel
import com.nitishsharma.frndcal.main.utils.Result
import javax.inject.Inject

class TaskListDefaultRepository @Inject constructor(
    val repository: TaskListRemoteRepository
) : TaskListRepository {
    override suspend fun getTaskList(taskListRequestModel: TaskListRequestModel): Result<AllTasks> {
        return repository.getTaskList(taskListRequestModel)
    }

    override suspend fun deleteTask(deleteTaskRequestModel: DeleteTaskRequestModel): Result<Unit> {
        return repository.deleteTask(deleteTaskRequestModel)
    }

}