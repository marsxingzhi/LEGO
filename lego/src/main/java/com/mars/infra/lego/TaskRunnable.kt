package com.mars.infra.lego

import com.mars.infra.lego.api.ITask
import com.mars.infra.lego.dispatch.TaskDispatcher

/**
 * Created by Mars on 2022/3/2
 */
class TaskRunnable(private val task: ITask<*>, private val dispatcher: TaskDispatcher): Runnable {

    override fun run() {
        task.run(dispatcher)
    }
}