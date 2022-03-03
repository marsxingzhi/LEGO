package com.mars.infra.lego.test

import android.util.Log
import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.ITask

/**
 * Created by Mars on 2022/3/3
 */
class DemoFourTask: AbstractTask<Unit>() {

    private var time: Long = 0

    override fun performTask(): Unit? {
        time -= System.currentTimeMillis()
        Thread.sleep(200)
        time += System.currentTimeMillis()
        Log.e("gy", "DemoFourTask performTask invoke, and spend $time ms")
        return null
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoSecondTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun getTaskName(): String {
        return DemoFourTask::class.java.name
    }
}