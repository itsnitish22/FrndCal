package com.nitishsharma.frndcal.main.tasklist.epoxy

import com.airbnb.epoxy.TypedEpoxyController
import com.nitishsharma.frndcal.main.tasklist.clickcallback.TaskListItemClickCallback
import com.nitishsharma.frndcal.main.tasklist.datamodel.AllTasks

class TaskListController(val itemClickCallback: TaskListItemClickCallback) :
    TypedEpoxyController<AllTasks>() {
    override fun buildModels(allTasks: AllTasks?) {
        allTasks?.taskList?.let { taskList ->
            taskList.forEachIndexed { index, task ->
                epoxyTaskListItem {
                    id("task-$index")
                    callback(this@TaskListController.itemClickCallback)
                    taskId(task.taskId ?: -1)
                    taskTitle(task.taskDetail?.taskTitle ?: "Dummy Title")
                    taskDescription(task.taskDetail?.taskDescription ?: "Dummy description")
                    taskDate(task.taskDetail?.taskDate ?: "01-01-2001")
                }
            }
            if (taskList.isEmpty()) {
                epoxyNoTaskItem {
                    id("no-task-found")
                }
            }
        }

    }
}