package com.mars.infra.lego.test.action2

import com.mars.infra.lego.annotations.Task
import com.mars.infra.lego.api.Action

/**
 * Created by Mars on 2022/3/8
 *
 * 利用注解，自动生成start task
 */
@Task(name = "FirstGenerateTask")
class FirstAction: Action<Boolean> {

    override fun performAction(): Boolean {
        Thread.sleep(1000)
        return true
    }
}