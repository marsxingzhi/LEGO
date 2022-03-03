package com.mars.infra.lego

/**
 * Created by Mars on 2022/3/2
 */
class TaskRunnable(private val task: ITask<*>): Runnable {

    override fun run() {
        task.run()
    }
}