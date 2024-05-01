package com.nitishsharma.frndcal.main.tasklist.datamodel

import com.google.gson.annotations.SerializedName

data class DeleteTaskRequestModel(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("task_id")
    val taskId: Int
)