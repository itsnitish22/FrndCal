package com.nitishsharma.frndcal.main.tasklist.clickcallback

import com.nitishsharma.frndcal.main.tasklist.TaskListViewModel

class TaskListItemClickCallback(val viewModel: TaskListViewModel) {
    fun deleteTask(taskId: Int) {
        viewModel.deleteTask(taskId)
    }
}