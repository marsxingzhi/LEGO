package com.mars.infra.lego.test.task

import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.api.ITask
import com.mars.infra.lego.test.action.SecondAction

/**
 * Created by Mars on 2022/3/2
 */
class DemoSecondTask: AbstractTask<Unit>() {

    override fun performTask() {
        val secondAction = SecondAction()
        secondAction.performAction()
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoFirstTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return true
    }

    override fun getTaskName(): String {
        return DemoSecondTask::class.java.name
    }
}