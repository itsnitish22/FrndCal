package com.nitishsharma.frndcal.main.tasklist.datamodel

import com.google.gson.annotations.SerializedName

data class TaskListRequestModel(
    @SerializedName("user_id")
    val userId: Int
)