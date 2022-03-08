package com.mars.infra.lego.test.task

import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.api.ITask
import com.mars.infra.lego.test.action.ThirdAction

/**
 * Created by Mars on 2022/3/2
 */
class DemoThirdTask: AbstractTask<Int>() {

    override fun performTask(): Int {
        val thirdAction = ThirdAction()
        return thirdAction.performAction()
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoFirstTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun getTaskName(): String {
        return DemoThirdTask::class.java.name
    }
}