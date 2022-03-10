package com.mars.infra.lego.annotation

/**
 * Created by Mars on 2022/3/10
 *
 * 是否在主线程中调用
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class WorkerThread(val callMainThread: Boolean = true)