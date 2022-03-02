package com.mars.infra.lego

import android.util.Log

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
}