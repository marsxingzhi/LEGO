package com.mars.infra.lego.test.task

import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.api.ITask
import com.mars.infra.lego.test.action.FiveAction

/**
 * Created by Mars on 2022/3/2
 */
class DemoFiveTask: AbstractTask<Unit>() {

    override fun performTask() {
        val fiveAction = FiveAction()
        fiveAction.performAction()
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoFourTask::class.java, DemoThirdTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun blockMainThread(): Boolean {
        return true
    }

    override fun getTaskName(): String {
        return DemoFiveTask::class.java.name
    }
}