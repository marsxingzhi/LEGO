package com.mars.infra.lego.test.task

import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.LegoExecutors
import com.mars.infra.lego.test.action.FirstAction
import java.util.concurrent.ExecutorService

/**
 * Created by geyan01 on 2/28/22
 */
class DemoFirstTask: AbstractTask<String>() {

    override fun performTask(): String {
        val firstAction = FirstAction()
        return firstAction.performAction()
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun getTaskName(): String {
        return DemoFirstTask::class.java.name
    }

    override fun createExecutor(): ExecutorService {
        return LegoExecutors.defaultExecutors
    }
}