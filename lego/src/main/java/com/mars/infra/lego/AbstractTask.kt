package com.mars.infra.lego

import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Mars on 2/28/22
 */
abstract class AbstractTask<T> : ITask<T>, IExecutor {

    private val preTaskCountDownLatch by lazy { CountDownLatch(getDependencySize()) }

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