package com.nitishsharma.frndcal.main.tasklist.datamodel

import com.google.gson.annotations.SerializedName
import com.nitishsharma.frndcal.main.home.datamodel.TaskModel

data class AllTasks(
    @SerializedName("tasks")
    val taskList: ArrayList<RemoteTask>?
)

data class RemoteTask(
    @SerializedName("task_id")
    val taskId: Int?,
    @SerializedName("task_detail")
    val taskDetail: TaskModel?
)