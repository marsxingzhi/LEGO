package com.mars.infra.lego.test.action2

import android.util.Log
import com.mars.infra.lego.annotation.DependOn
import com.mars.infra.lego.annotation.TYPE_ANY
import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.annotation.WorkerThread
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/10
 */
@Task(name = "FiveGenerateTask", typeVariable = TYPE_ANY)
@DependOn(dependencies = ["com.mars.infra.lego.test.action2.FourAction"])
class FiveAction : Action<Any> {

    override fun performAction(): Any {
        Log.e("mars", "FiveAction---performAction---start")
        Thread.sleep(30)
        Log.e("mars", "FiveAction---performAction---end")
        return Any()
    }
}