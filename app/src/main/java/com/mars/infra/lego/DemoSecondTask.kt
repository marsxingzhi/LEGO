package com.mars.infra.lego

import android.util.Log

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
}