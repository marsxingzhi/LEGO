package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.BlockMainThread
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import javax.lang.model.element.TypeElement

/**
 * Created by Mars on 2022/3/10
 */
class BlockMainThreadBuilder(element: TypeElement) {

    init {
        val annotation = element.getAnnotation(BlockMainThread::class.java)
        println("BlockMainThreadBuilder#init, element = $element, annotation = $annotation")
    }

    private val blockMainThreadFunSpec by lazy {
        FunSpec.builder("blockMainThread")
            .addModifiers(KModifier.OVERRIDE)
            .returns(Boolean::class)
            .addStatement("return %L", "true")  // 如果存在实例对象，那么一定是true
            .build()
    }

    fun build(): FunSpec = blockMainThreadFunSpec
}