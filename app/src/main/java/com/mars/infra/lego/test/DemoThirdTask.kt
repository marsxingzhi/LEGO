package com.mars.infra.lego.test

import android.util.Log
import com.mars.infra.lego.AbstractTask
import com.mars.infra.lego.ITask

/**
 * Created by Mars on 2022/3/2
 */
class DemoThirdTask: AbstractTask<Int>() {

    private var time: Long = 0

    override fun performTask(): Int {
        time -= System.currentTimeMillis()
        Thread.sleep(300)
        time += System.currentTimeMillis()

        Log.e("gy", "DemoThirdTask performTask invoke, and spend $time ms")
        return -1
    }

    override fun dependOn(): List<Class<out ITask<*>>> {
        return arrayListOf(DemoFirstTask::class.java)
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun getTaskName(): String {
        return DemoThirdTask::class.java.name
    }
}