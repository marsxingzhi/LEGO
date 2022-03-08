package com.mars.infra.lego.test.action

import android.util.Log
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/8
 */
class ThirdAction: Action<Int> {

    private var time: Long = 0

    override fun performAction(): Int {
        time -= System.currentTimeMillis()
        Thread.sleep(300)
        time += System.currentTimeMillis()

        Log.e("mars", "ThirdAction performTask invoke, and spend $time ms")
        return -1
    }

}