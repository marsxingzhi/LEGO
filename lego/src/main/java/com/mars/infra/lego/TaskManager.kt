package com.mars.infra.lego

import com.mars.infra.lego.sort.TopologyStrategy
import java.util.concurrent.Executors

/**
 * Created by Mars on 2/28/22
 */
class TaskManager private constructor(builder: Builder) {

    private val mTaskList by lazy { mutableListOf<ITask<*>>() }
//    private val mExecutors by lazy { Executors.newCachedThreadPool() }

    init {
        mTaskList.addAll(builder.taskList)
    }

    fun start() {
        TopologyStrategy.sort(mTaskList).result
            .filterIsInstance(AbstractTask::class.java)
            .forEach {
            if (!it.callOnMainThread()) {
//                mExecutors.execute(TaskRunnable(it))
                it.createExecutor().execute(TaskRunnable(it))
            } else {
                it.run()
            }
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