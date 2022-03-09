package com.mars.infra.lego.test.action2

import android.util.Log
import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/9
 */
@Task(
    name = "SecondGenerateTask",
    typeVariable = "kotlin.Unit",
    dependOn = ["com.mars.infra.lego.test.action2.ThirdAction", "com.mars.infra.lego.test.action2.FirstAction"])
class SecondAction: Action<Unit> {

    override fun performAction() {
        Log.e("mars", "SecondAction performAction start")
        Thread.sleep(5000)
        Log.e("mars", "SecondAction performAction end")
    }
}