package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.compiler.ext.no
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer
import javax.lang.model.element.TypeElement
import kotlin.collections.HashMap

/**
 * Created by Mars on 2022/3/8
 */
class TaskBuilder(element: TypeElement, taskActionMap: HashMap<String, String>) {

    private var generateTaskName: String

    //    private var typeVariable: KClass<out Any>
    private var typeVariable: String  // 因为apt过程没有KClass或者Class的概念，因此只能通过字符串传入

    init {
        println("TaskBuilder, element = $element")
        val annotation = element.getAnnotation(Task::class.java)
        generateTaskName = annotation.name
        typeVariable = annotation.typeVariable

        println("TaskBuilder#init, generateTaskName = $generateTaskName, typeVariable = $typeVariable")

        taskActionMap[element.toString()] = generateTaskName
    }

    private val actionClassName = element.asClassName()

    private val performTaskFunSpecBuilder by lazy {
        FunSpec.builder("performTask")
            .addModifiers(KModifier.OVERRIDE)
            .returns(mapClassName(typeVariable))
            .addStatement("val action = %T()", actionClassName)
            .addStatement("val result = action.%L()", "performAction")  // %L for Literals
            .addStatement("return %L", "result")
    }

    private val getTaskNameFunSpecBuilder by lazy {
        FunSpec.builder("getTaskName")
            .addModifiers(KModifier.OVERRIDE)
            .returns(String::class)
            .addStatement("return %T::class.java.name", ClassName(PACKAGE_NAME, generateTaskName))
    }


    private var mDependencyBuilder: DependencyBuilder? = null
    private var mWorkerThreadBuilder: WorkerThreadBuilder? = null
    private var mBlockMainThreadBuilder: BlockMainThreadBuilder? = null


    fun setBuilders(dependencyBuilder: DependencyBuilder?,
                    workerThreadBuilder: WorkerThreadBuilder?,
                    blockMainThreadBuilder: BlockMainThreadBuilder?) {
        mDependencyBuilder = dependencyBuilder
        mWorkerThreadBuilder = workerThreadBuilder
        mBlockMainThreadBuilder = blockMainThreadBuilder
    }

    fun build(filer: Filer) {
        val file = FileSpec.builder(PACKAGE_NAME, generateTaskName)
            .addType(buildTypeSpec())
            .build()
        file.writeTo(filer)
    }

    private fun buildTypeSpec(): TypeSpec {
        val typeSpecBuilder = TypeSpec.classBuilder(generateTaskName)
            .superclass(abstractTaskClassName.parameterizedBy(mapClassName(typeVariable)))  // 泛型，例如：String::class.asClassName()
            .addFunction(performTaskFunSpecBuilder.build())
            .addFunction(getTaskNameFunSpecBuilder.build())

        // 默认时true，只有等于false时，子类复写
        mWorkerThreadBuilder?.callMainThread?.no {
            typeSpecBuilder.addFunction(mWorkerThreadBuilder!!.build())
        }

        // 默认是false，只有等于true时，子类复写
        mBlockMainThreadBuilder?.let {
            typeSpecBuilder.addFunction(it.build())
        }

        mDependencyBuilder?.let { typeSpecBuilder.addFunction(it.build()) }
        return typeSpecBuilder.build()
    }

    private fun mapClassName(typeVariable: String) = when (typeVariable) {
        BOOLEAN.canonicalName -> BOOLEAN
        BYTE.canonicalName -> BYTE
        SHORT.canonicalName -> SHORT
        INT.canonicalName -> INT
        LONG.canonicalName -> LONG
        CHAR.canonicalName -> CHAR
        FLOAT.canonicalName -> FLOAT
        DOUBLE.canonicalName -> DOUBLE
        STRING.canonicalName -> STRING
        ARRAY.canonicalName -> ARRAY
        UNIT.canonicalName -> UNIT
        else -> ANY
    }

    companion object {
        const val PACKAGE_NAME = "com.mars.infra.lego.generate"
    }
}