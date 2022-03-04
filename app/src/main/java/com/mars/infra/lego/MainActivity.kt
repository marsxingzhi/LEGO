package com.mars.infra.lego

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mars.infra.lego.test.*

class MainActivity : AppCompatActivity() {

    lateinit var mBtnTestTopology: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnTestTopology = findViewById(R.id.btn_test_topology)

        mBtnTestTopology.setOnClickListener {
            testTopology()
        }
    }

    private fun testTopology() {
        TaskManager.Builder()
            .addTask(DemoFirstTask())
            .addTask(DemoFiveTask())
            .addTask(DemoFourTask())
            .addTasks(arrayListOf(DemoSecondTask(), DemoThirdTask()))
            .build()
            .start()
            .awaitMainThread()
    }

}