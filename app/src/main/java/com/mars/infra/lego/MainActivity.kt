package com.mars.infra.lego

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mars.infra.lego.generate.FirstGenerateTask
import com.mars.infra.lego.generate.SecondGenerateTask
import com.mars.infra.lego.generate.ThirdGenerateTask
import com.mars.infra.lego.test.task.*

class MainActivity : AppCompatActivity() {

    lateinit var mBtnTestTopology: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnTestTopology = findViewById(R.id.btn_test_topology)

        mBtnTestTopology.setOnClickListener {
            Log.e("mars", "before testTopology")
            testTopology()
            Log.e("mars", "after testTopology")
        }
    }

    private fun testTopology() {
        TaskManager.Builder()
//            .addTask(DemoFirstTask())
//            .addTask(DemoFiveTask())
//            .addTask(DemoFourTask())
//            .addTasks(arrayListOf(DemoSecondTask(), DemoThirdTask()))
            .addTask(SecondGenerateTask())
            .addTask(ThirdGenerateTask())
            .addTask(FirstGenerateTask())
            .build()
            .start()
            .awaitMainThread()
    }

}