package com.mars.infra.lego

import com.mars.infra.lego.dispatch.TaskDispatcher

/**
 * Created by Mars on 2/28/22
 */
interface ITask<T> {

    /**
     * 任务执行
     */
    fun run(dispatcher: TaskDispatcher): T?

    /**
     * 当前任务依赖哪些任务，即前序任务
     */
    fun dependOn(): List<Class<out ITask<*>>>?

    /**
     * 前序任务数
     */
    fun getDependencySize(): Int

    /**
     * 是否在主线程中执行
     */
    fun callOnMainThread(): Boolean

    /**
     * 是否阻塞主线程，如果返回true，表示主线程需等待该任务完成后，才能执行
     */
    fun blockMainThread(): Boolean

    /**
     * 阻塞当前任务执行
     */
    fun onWait()

    /**
     * 当前序任务完成时，提醒当前任务
     */
    fun onNotify()

    fun getTaskName(): String

}