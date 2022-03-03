package com.mars.infra.lego

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Mars on 2022/3/2
 */
interface IExecutor {

    fun createExecutor(): ExecutorService
}

// TODO 暂时默认
object LegoExecutors {

    val ioExecutor = Executors.newCachedThreadPool()

    val defaultExecutors = Executors.newCachedThreadPool()

}