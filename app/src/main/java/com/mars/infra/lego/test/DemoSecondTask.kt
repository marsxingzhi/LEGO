package com.mars.infra.lego.test

import android.util.Log
import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.ITask

/**
 * Created by Mars on 2022/3/2
 */
class DemoSecondTask: AbstractTask<Unit>() {

    private var time: Long = 0

    override fun performTask(): Unit? {
        time -= System.currentTimeMillis()
        Thread.sleep(200)
        time += System.currentTimeMillis()
        Log.e("gy", "DemoSecondTask performTask invoke, and spend $time ms")
        return null
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoFirstTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return true
    }

    override fun getTaskName(): String {
        return DemoSecondTask::class.java.name
    }
}