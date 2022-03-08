package com.mars.infra.lego.test.action

import android.util.Log
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/8
 *
 * 真正的任务执行，只专注于任务执行这一件事
 */
class FirstAction : Action<String> {

    private var time: Long = 0

    override fun performAction(): String {
        time -= System.currentTimeMillis()
        Thread.sleep(100)
        time += System.currentTimeMillis()

        Log.e("mars", "FirstAction performTask invoke, and spend $time ms")
        return "FirstAction"
    }
}