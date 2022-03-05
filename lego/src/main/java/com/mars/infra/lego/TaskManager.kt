package com.mars.infra.lego

import android.os.Looper
import android.util.Log
import com.mars.infra.lego.dispatch.TaskDispatcher
import com.mars.infra.lego.sort.TopologyStrategy
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Mars on 2/28/22
 */
class TaskManager private constructor(builder: Builder) {

    private val mTaskList by lazy { mutableListOf<ITask<*>>() }

    //    private val mExecutors by lazy { Executors.newCachedThreadPool() }
    private val mDispatcher by lazy { TaskDispatcher() }
    private val mNeedWaitMainThreadCount = AtomicInteger()
    private lateinit var awaitMainThreadCountDown: CountDownLatch

    init {
        mTaskList.addAll(builder.taskList)
        mTaskList.forEach {
            if (!it.callOnMainThread() && it.blockMainThread()) {
                mNeedWaitMainThreadCount.incrementAndGet()
                Log.e("gy", "TaskManager---mNeedWaitMainThreadCount = ${mNeedWaitMainThreadCount.get()}")
            }
        }
    }

    fun start(): TaskManager {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw Exception("start method must be invoked in main thread")
        }
//        TopologyStrategy.sort(mTaskList).result
//            .filterIsInstance(AbstractTask::class.java)
//            .forEach {
//            if (!it.callOnMainThread()) {
////                mExecutors.execute(TaskRunnable(it))
//                it.createExecutor().execute(TaskRunnable(it))
//            } else {
//                it.run()
//            }
//        }
        awaitMainThreadCountDown = CountDownLatch(mNeedWaitMainThreadCount.get())

        TopologyStrategy.sort(mTaskList).run {
            mDispatcher.prepare(this, awaitMainThreadCountDown, mNeedWaitMainThreadCount)
            this.result.filterIsInstance(AbstractTask::class.java).run {
                mDispatcher.dispatch(this)
            }
        }
        return this
    }

    /**
     * 阻塞主线程
     */
    fun awaitMainThread() {
       try {
           // await，超时操作
           awaitMainThreadCountDown.await(10000, TimeUnit.MILLISECONDS)
       } catch (e: Exception) {
           e.printStackTrace()
       }
    }


    class Builder {

        val taskList = mutableListOf<ITask<*>>()

        fun addTask(task: ITask<*>): Builder = apply {
            taskList.add(task)
        }

        fun addTasks(tasks: List<ITask<*>>): Builder = apply {
            taskList.addAll(tasks)
        }

        fun build(): TaskManager {
            return TaskManager(this)
        }
    }

}