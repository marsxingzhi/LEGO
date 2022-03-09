package com.mars.infra.lego.test.action2

import android.util.Log
import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/9
 */
@Task(name = "ThirdGenerateTask", typeVariable = "kotlin.Int")
class ThirdAction: Action<Int> {

    override fun performAction(): Int {
        Log.e("mars", "ThirdAction performAction start")
        Thread.sleep(2000)
        Log.e("mars", "ThirdAction performAction end")
        return -1
    }
}