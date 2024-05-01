package com.nitishsharma.frndcal.main.tasklist.repository

import com.nitishsharma.frndcal.main.tasklist.datamodel.AllTasks
import com.nitishsharma.frndcal.main.tasklist.datamodel.DeleteTaskRequestModel
import com.nitishsharma.frndcal.main.tasklist.datamodel.TaskListRequestModel
import com.nitishsharma.frndcal.main.utils.Result
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

class TaskListRemoteRepository @Inject constructor(
    retrofit: Retrofit
) {

    private val taskListService = retrofit.create(TaskListAPIService::class.java)
    suspend fun getTaskList(taskListRequestModel: TaskListRequestModel): Result<AllTasks> {
        return try {
            val response = taskListService.getTaskList(taskListRequestModel)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteTask(deleteTaskRequestModel: DeleteTaskRequestModel): Result<Unit> {
        return try {
            val response = taskListService.deleteTask(deleteTaskRequestModel)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    interface TaskListAPIService {
        @POST("api/getCalendarTaskList")
        suspend fun getTaskList(@Body taskListRequestModel: TaskListRequestModel): AllTasks

        @POST("api/deleteCalendarTask")
        suspend fun deleteTask(@Body deleteTaskRequestModel: DeleteTaskRequestModel): Unit
    }
}