package com.nitishsharma.frndcal.main.tasklist.epoxy

import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.DataBindingEpoxyModel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.nitishsharma.frndcal.BR
import com.nitishsharma.frndcal.R
import com.nitishsharma.frndcal.main.tasklist.clickcallback.TaskListItemClickCallback

@EpoxyModelClass
abstract class EpoxyTaskListItem : DataBindingEpoxyModel() {
    @EpoxyAttribute
    lateinit var callback: TaskListItemClickCallback

    @EpoxyAttribute
    var taskId: Int = -1

    @EpoxyAttribute
    var taskTitle: String = ""

    @EpoxyAttribute
    var taskDescription: String = ""

    @EpoxyAttribute
    var taskDate: String = ""

    override fun getDefaultLayout(): Int {
        return R.layout.recycler_task_item
    }

    override fun setDataBindingVariables(binding: ViewDataBinding) {
        binding.setVariable(BR.callback, callback)
        binding.setVariable(BR.taskId, taskId)
        binding.setVariable(BR.taskTitle, taskTitle)
        binding.setVariable(BR.taskDescription, taskDescription)
        binding.setVariable(BR.taskDate, taskDate)
    }
}

@EpoxyModelClass
abstract class EpoxyNoTaskItem : DataBindingEpoxyModel() {

    override fun getDefaultLayout(): Int {
        return R.layout.recycler_no_task_item
    }

    override fun setDataBindingVariables(binding: ViewDataBinding) {
    }
}