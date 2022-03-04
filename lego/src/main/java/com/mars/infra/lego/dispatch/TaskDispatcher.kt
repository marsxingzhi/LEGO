package com.mars.infra.lego.dispatch

import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.TaskRunnable
import com.mars.infra.lego.sort.TaskResult

/**
 * Created by Mars on 2022/3/4
 */
class TaskDispatcher : IDispatcher {

    lateinit var mTaskResult: TaskResult

    override fun prepare(taskResult: TaskResult) {
        mTaskResult = taskResult
    }

    override fun dispatch(tasks: List<AbstractTask<*>>) {
        tasks.forEach {
            val runnable = TaskRunnable(it, this)
            if (!it.callOnMainThread()) {
                it.createExecutor().execute(runnable)
            } else {
                runnable.run()
            }
        }
    }

    fun notifyPostTask(currentTaskName: String) {
        val postTaskList = mTaskResult.postTaskMap[currentTaskName]
        // 拿到依赖currentTaskName任务的任务
        postTaskList?.forEach {
            val postTask = mTaskResult.taskMap[it]
            postTask?.onNotify()
        }
    }

}

interface IDispatcher {

    fun prepare(taskResult: TaskResult)

    fun dispatch(tasks: List<AbstractTask<*>>)
}