package com.mars.infra.lego

import com.mars.infra.lego.api.ITask
import com.mars.infra.lego.dispatch.TaskDispatcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService

/**
 * Created by Mars on 2/28/22
 */
abstract class AbstractTask<T> : ITask<T>, IExecutor {

    private val preTaskCountDownLatch by lazy { CountDownLatch(getDependencySize()) }
    private lateinit var mDispatcher: TaskDispatcher

    /**
     * 任务执行，可分成3个阶段
     */
    override fun run(dispatcher: TaskDispatcher): T? {
        mDispatcher = dispatcher
        prePerformTask()
        val result = performTask()
        postPerformTask()
        return result
    }

    abstract fun performTask(): T

    private fun postPerformTask() {
//        onNotify()
        // 当前任务执行完成后，需要告诉其后序任务，后序任务的计数器需减1
        mDispatcher.notifyPostTask(getTaskName())
    }

    private fun prePerformTask() {
        // 先执行wait操作，如果计数器为0，则直接执行任务；如果不为0，会阻塞住当前任务的执行
        onWait()
    }


    override fun dependOn(): List<Class<out ITask<*>>>? {
        return null
    }

    override fun getDependencySize(): Int {
        return dependOn()?.size ?: 0
    }

    override fun callOnMainThread(): Boolean {
        return true
    }

    override fun blockMainThread(): Boolean {
        return false
    }

    override fun onWait() {
        preTaskCountDownLatch.await()
    }

    override fun onNotify() {
        preTaskCountDownLatch.countDown()
    }

    /**
     * 该任务需要在哪个线程池中执行，默认io
     */
    override fun createExecutor(): ExecutorService = LegoExecutors.ioExecutor
}