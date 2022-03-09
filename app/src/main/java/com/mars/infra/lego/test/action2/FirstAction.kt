package com.mars.infra.lego.test.action2

import android.util.Log
import com.mars.infra.lego.annotation.TYPE_BOOLEAN
import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/8
 *
 * 利用注解，自动生成start task
 */
@Task(name = "FirstGenerateTask", typeVariable = TYPE_BOOLEAN)
class FirstAction: Action<Boolean> {

    override fun performAction(): Boolean {
        Log.e("mars", "FirstAction performAction start")
        Thread.sleep(1000)
        Log.e("mars", "FirstAction performAction end")
        return true
    }
}