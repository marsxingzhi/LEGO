package com.mars.infra.lego.sort

import com.mars.infra.lego.api.ITask
import java.util.ArrayDeque

/**
 * Created by Mars on 2022/3/2
 */
object TopologyStrategy {

    /**
     * 拓扑排序，可以分成三个步骤：
     * 1. 生成一张表，当前任务A与依赖任务A的任务集合，即<Task, List<Task>>。在生成表的过程中，判断Task的入度，如果为0，则添加到zeroDeque中
     * 2. 处理0入度
     * 3. 更新表，即需要将依赖任务A的任务的入度都减去1，此时若有0入度的，将继续添加到zeroDeque中
     */
    fun sort(taskList: List<ITask<*>>): TaskResult {

        val ioResult = mutableListOf<ITask<*>>()
        val mainResult = mutableListOf<ITask<*>>()

        val zeroDeque = ArrayDeque<String>()  // 入度为0的task
        val inDegreeMap = hashMapOf<String, Int>()  // task对应的入度数
        val taskMap = hashMapOf<String, ITask<*>>()  // key-task
        val postTaskMap = hashMapOf<String, MutableList<String>>()  // 当前任务-当前任务的后序任务

        /**
         * 1. 将TaskName-Task加入到taskMap中
         * 2. 0入度的Task，加入到队列中
         * 3. 前序任务与当前任务的对应关系，最终形成每一个任务都与依赖该任务的任务链表
         */
        taskList.forEach { task ->
            val taskName = task.getTaskName()
            if (!taskMap.containsKey(taskName)) {
                taskMap[taskName] = task
                inDegreeMap[taskName] = task.getDependencySize()
                if (task.dependOn().isNullOrEmpty()) {
                    // 0入度
                    zeroDeque.offer(taskName)
                } else {
                    task.dependOn()!!.forEach { preTask ->
                        val key = preTask.name
                        val values = postTaskMap.getOrDefault(key, mutableListOf())
                        values.add(taskName)
                        postTaskMap[key] = values
                    }
                }
            } else {
                throw Exception("重复添加任务")
            }
        }

        while (zeroDeque.isNotEmpty()) {
            zeroDeque.poll()?.let {
                // 将任务添加到链表中
                taskMap[it]?.let { task ->
                    /**
                     * 区分mainResult和ioResult，主要是做拓扑优化。
                     * 启动Task都是在主线程触发的，即调用start方法，开始分发Task并执行。
                     * 假设TaskB和TaskC都依赖TaskA的完成，TaskB需要在主线程中执行，TaskC需要在子线程中执行
                     * 拓扑排序的结果是不固定的，那么此时可能存在TaskA ---> TaskB ---> TaskC，或者TaskA ---> TaskC ---> TaskB
                     * 如果是第一种情况，假设TaskB执行耗时，这时候就阻塞了TaskC的执行，因为无法分发了，即出现了同步任务阻塞异步任务的现象
                     * 本来TaskB和TaskC可以并发执行的，而此时就变成了TaskB和TaskC串行
                     *
                     * 因此，尽可能的将在子线程执行的任务添加到前面
                     */
                    if (task.callOnMainThread()) {
                        mainResult.add(task)
                    } else {
                        ioResult.add(task)
                    }
                }
                // 更新表的入度
                postTaskMap[it]?.forEach { postTask ->
                    val inDegree = inDegreeMap[postTask]
                    inDegreeMap[postTask] = inDegree?.minus(1) ?: 0
                    if (inDegreeMap[postTask] == 0) {
                        zeroDeque.offer(postTask)
                    }
                }
            }
        }

        if (mainResult.size + ioResult.size != taskList.size) {
            throw Exception("依赖数不对，可能缺少依赖，或者存在循环依赖")
        }

        val result = mutableListOf<ITask<*>>().apply {
            addAll(ioResult)
            addAll(mainResult)
        }

        return TaskResult(result, taskMap, postTaskMap)
    }
}

data class TaskResult(
    val result: List<ITask<*>>,
    val taskMap: HashMap<String, ITask<*>>,
    val postTaskMap: HashMap<String, MutableList<String>>
)