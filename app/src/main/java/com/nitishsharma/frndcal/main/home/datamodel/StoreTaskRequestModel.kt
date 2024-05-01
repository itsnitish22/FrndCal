package com.nitishsharma.frndcal.main.home.datamodel

import com.google.gson.annotations.SerializedName

data class StoreTaskRequestModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task")
    val task: TaskModel
)

data class TaskModel(
    @SerializedName("title")
    val taskTitle: String?,
    @SerializedName("description")
    val taskDescription: String?,
    @SerializedName("date")
    val taskDate: String?
)