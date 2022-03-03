package com.mars.infra.lego.test

import android.util.Log
import com.mars.infra.lego.AbstractTask

/**
 * Created by geyan01 on 2/28/22
 */
class DemoFirstTask: AbstractTask<String>() {

    private var time: Long = 0

    override fun performTask(): String {
        time -= System.currentTimeMillis()
        Thread.sleep(100)
        time += System.currentTimeMillis()

        Log.e("gy", "DemoFirstTask performTask invoke, and spend $time ms")
        return "DemoFirstTask"
    }

    override fun callOnMainThread(): Boolean {
        return false
    }

    override fun getTaskName(): String {
        return DemoFirstTask::class.java.name
    }
}