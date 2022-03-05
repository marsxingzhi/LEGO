package com.mars.infra.lego.dispatch

import android.util.Log
import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.TaskRunnable
import com.mars.infra.lego.ext.no
import com.mars.infra.lego.ext.otherwise
import com.mars.infra.lego.ext.yes
import com.mars.infra.lego.sort.TaskResult
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Mars on 2022/3/4
 */
class TaskDispatcher : IDispatcher {

    lateinit var mTaskResult: TaskResult
    // 阻塞主线程
    lateinit var mAwaitCountDownLatch: CountDownLatch
    lateinit var mAwaitCountDown: AtomicInteger

    override fun prepare(taskResult: TaskResult, countDownLatch: CountDownLatch, awaitCountDown: AtomicInteger) {
        mTaskResult = taskResult
        mAwaitCountDownLatch = countDownLatch
        mAwaitCountDown = awaitCountDown
    }

    override fun dispatch(tasks: List<AbstractTask<*>>) {
        tasks.forEach {
            val runnable = TaskRunnable(it, this)
            it.callOnMainThread().no {
                it.createExecutor().execute(runnable)
            }.otherwise {
                runnable.run()
            }
        }
    }

    /**
     * 1. 阻塞主线程的CountDownLatch减一
     * 2. 子线程
     */
    fun notifyPostTask(currentTaskName: String) {
        // 1. 处理主线程的CountDownLatch，异步任务执行完成后，需要告诉主线程，可能主线程阻塞着
        val currentTask = mTaskResult.taskMap[currentTaskName] as AbstractTask
        (!currentTask.callOnMainThread() && currentTask.blockMainThread()).yes {
            // 处理一下
            mAwaitCountDown.decrementAndGet()
            mAwaitCountDownLatch.countDown()
            Log.e("gy", "TaskDispatcher---notifyPostTask mAwaitCountDown value = ${mAwaitCountDown.get()}")
        }

        // 2. 处理后序任务
        val postTaskList = mTaskResult.postTaskMap[currentTaskName]
        // 拿到依赖currentTaskName任务的任务
        postTaskList?.forEach {
            val postTask = mTaskResult.taskMap[it]
            postTask?.onNotify()
        }
    }

}

interface IDispatcher {

    fun prepare(taskResult: TaskResult, countDownLatch: CountDownLatch, awaitCountDown: AtomicInteger)

    fun dispatch(tasks: List<AbstractTask<*>>)
}