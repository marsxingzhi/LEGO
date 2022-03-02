package com.mars.infra.lego.sort

import com.mars.infra.lego.ITask

/**
 * Created by Mars on 2022/3/2
 */
object TopologyStrategy {

    fun sort(list: List<ITask<*>>?): List<ITask<*>> {
        val innerList = mutableListOf<ITask<*>>()
        list?.let { innerList.addAll(it) }
        return innerList
    }
}