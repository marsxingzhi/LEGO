package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.WorkerThread
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import javax.lang.model.element.TypeElement

/**
 * Created by Mars on 2022/3/10
 */
class WorkerThreadBuilder(element: TypeElement) {

    var callMainThread: Boolean
        private set

    init {
        val annotation = element.getAnnotation(WorkerThread::class.java)
        callMainThread = annotation.callMainThread
    }


    private val callMainThreadFunSpec by lazy {
        FunSpec.builder("callOnMainThread")
            .addModifiers(KModifier.OVERRIDE)
            .returns(Boolean::class)
            .addStatement("return %L", callMainThread)
            .build()
    }

    fun build(): FunSpec {
        return callMainThreadFunSpec
    }
}