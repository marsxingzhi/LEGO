package com.mars.infra.lego

import android.app.Application

/**
 * Created by Mars on 2022/3/2
 */
class LegoApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        TaskManager.Builder()
            .addTask(DemoFirstTask())
            .addTasks(arrayListOf(DemoSecondTask(), DemoThirdTask()))
            .build()
            .start()
    }
}