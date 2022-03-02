package com.mars.infra.lego

import android.util.Log

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
}