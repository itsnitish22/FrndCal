package com.nitishsharma.frndcal.main.tasklist.repository

import com.nitishsharma.frndcal.main.tasklist.datamodel.AllTasks
import com.nitishsharma.frndcal.main.tasklist.datamodel.DeleteTaskRequestModel
import com.nitishsharma.frndcal.main.tasklist.datamodel.TaskListRequestModel
import com.nitishsharma.frndcal.main.utils.Result

interface TaskListRepository {
    suspend fun getTaskList(taskListRequestModel: TaskListRequestModel): Result<AllTasks>
    suspend fun deleteTask(deleteTaskRequestModel: DeleteTaskRequestModel): Result<Unit>
}