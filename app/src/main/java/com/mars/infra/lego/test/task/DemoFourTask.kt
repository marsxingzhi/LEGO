package com.mars.infra.lego.test.task

import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.api.ITask
import com.mars.infra.lego.test.action.FourAction

/**
 * Created by Mars on 2022/3/3
 */
class DemoFourTask: AbstractTask<Unit>() {

    override fun performTask() {
        val fourAction = FourAction()
        fourAction.performAction()
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoSecondTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun getTaskName(): String {
        return DemoFourTask::class.java.name
    }
}