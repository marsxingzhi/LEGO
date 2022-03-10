package com.mars.infra.lego.test.action2

import android.util.Log
import com.mars.infra.lego.annotation.DependOn
import com.mars.infra.lego.annotation.TYPE_UNIT
import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.annotation.WorkerThread
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/10
 */
@Task(name = "FourGenerateTask", typeVariable = TYPE_UNIT)
@DependOn(dependencies = ["com.mars.infra.lego.test.action2.SecondAction"])
class FourAction : Action<Unit> {

    override fun performAction() {
        Log.e("mars", "FourAction---performAction---start")
        Thread.sleep(30)
        Log.e("mars", "FourAction---performAction---end")
    }
}