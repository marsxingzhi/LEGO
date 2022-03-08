package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.Task
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer
import javax.lang.model.element.TypeElement

/**
 * Created by Mars on 2022/3/8
 */
class TaskBuilder(private val element: TypeElement) {

    private var generateTaskName: String

    //    private var typeVariable: KClass<out Any>
    private var typeVariable: String  // 因为apt过程没有KClass或者Class的概念，因此只能通过字符串传入

    init {
        val annotation = element.getAnnotation(Task::class.java)
        generateTaskName = annotation.name
        typeVariable = annotation.typeVariable

        println("TaskBuilder---generateTaskName = $generateTaskName, typeVariable = $typeVariable, BOOLEAN = ${BOOLEAN.canonicalName}")
    }

    private val abstractTaskClassName = ClassName("com.mars.infra.lego", "AbstractTask")

    fun build(filer: Filer) {
        val file = FileSpec.builder(PACKAGE_NAME, generateTaskName)
            .addType(buildTypeSpec())
            .build()
        file.writeTo(filer)
    }

    private fun buildTypeSpec(): TypeSpec {
        return TypeSpec.classBuilder(generateTaskName)
            .superclass(abstractTaskClassName.parameterizedBy(mapClassName(typeVariable)))  // 泛型，例如：String::class.asClassName()
            .addFunction(
                FunSpec.builder("test").build()
            )
            .addFunction(
                FunSpec.builder("performTask")
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(mapClassName(typeVariable))
                    .addStatement("return %L", "true")  // TODO 暂时写成true
                    .build()
            )
            .addFunction(
                FunSpec.builder("getTaskName")
                    .addModifiers(KModifier.OVERRIDE)
                    .returns(String::class)
                    .addStatement("return %S", generateTaskName)
                    .build()
            )
            .build()
    }

    private fun mapClassName(typeVariable: String) = when (typeVariable) {
        BOOLEAN.canonicalName -> BOOLEAN
        STRING.canonicalName -> STRING
        INT.canonicalName -> INT
        else -> ANY
    }

    companion object {
        const val PACKAGE_NAME = "com.mars.infra.lego.generate"
    }
}