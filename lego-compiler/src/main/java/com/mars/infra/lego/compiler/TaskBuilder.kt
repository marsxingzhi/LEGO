package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.compiler.ext.no
import com.mars.infra.lego.compiler.ext.yes
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.Filer
import javax.lang.model.element.TypeElement
import kotlin.collections.HashMap

/**
 * Created by Mars on 2022/3/8
 */
class TaskBuilder(private val element: TypeElement, private val taskActionMap: HashMap<String, String>) {

    private var generateTaskName: String

    //    private var typeVariable: KClass<out Any>
    private var typeVariable: String  // 因为apt过程没有KClass或者Class的概念，因此只能通过字符串传入
    private var dependOnArray: Array<String>
    private var callMainThread: Boolean
    private var blockMainThread: Boolean

    init {
        val annotation = element.getAnnotation(Task::class.java)
        generateTaskName = annotation.name
        typeVariable = annotation.typeVariable
        dependOnArray = annotation.dependOn
        callMainThread = annotation.callMainThread
        blockMainThread = annotation.blockMainThread

        println("TaskBuilder#init, generateTaskName = $generateTaskName, typeVariable = $typeVariable, callMainThread = $callMainThread, blockMainThread = $blockMainThread")

        dependOnArray.forEach {
            // TaskBuilder---dependOnArray = com.mars.infra.lego.test.action2.ThirdAction
            println("TaskBuilder#init, dependOnArray = $it")
        }
        taskActionMap[element.toString()] = generateTaskName
    }

    private val abstractTaskClassName = ClassName("com.mars.infra.lego", "AbstractTask")
    private val actionClassName = element.asClassName()
    private val listClassName = ClassName("kotlin.collections", "List")
    private val clzClassName = ClassName("java.lang", "Class")
    private val iTaskClassName = ClassName("com.mars.infra.lego.api", "ITask")
    private val arrayListClassName = ClassName("kotlin.collections", "ArrayList")
    private val t = TypeVariableName("*")


    private val dependOnFunSpecBuilder by lazy {
        FunSpec.builder("dependOn")
            .addModifiers(KModifier.OVERRIDE)
            .returns(listClassName.parameterizedBy(clzClassName.parameterizedBy(WildcardTypeName.producerOf(iTaskClassName.parameterizedBy(t)))))  // List<Class<out ITask<*>>>
            .addStatement("val list = %T()", arrayListClassName.parameterizedBy(clzClassName.parameterizedBy(WildcardTypeName.producerOf(iTaskClassName.parameterizedBy(t)))))
    }

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

    private val callMainThreadFunSpec by lazy {
        FunSpec.builder("callOnMainThread")
            .addModifiers(KModifier.OVERRIDE)
            .returns(Boolean::class)
            .addStatement("return %L", callMainThread)
            .build()
    }

    private val blockMainThreadFunSpec by lazy {
        FunSpec.builder("blockMainThread")
            .addModifiers(KModifier.OVERRIDE)
            .returns(Boolean::class)
            .addStatement("return %L", blockMainThread)
            .build()
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
            .addFunction(
                FunSpec.builder("test").build()
            )
            .addFunction(performTaskFunSpecBuilder.build())
            .addFunction(getTaskNameFunSpecBuilder.build())

        // 默认时true，只有等于false时，子类复写
        callMainThread.no {
            typeSpecBuilder.addFunction(callMainThreadFunSpec)
        }
        // 默认是false，只有等于true时，子类复写
        blockMainThread.yes {
            typeSpecBuilder.addFunction(blockMainThreadFunSpec)
        }

        dependOnArray.isNotEmpty().yes {
            val dependOnList = arrayListOf<String>()
            dependOnArray.forEach { action ->
                taskActionMap[action]?.let { task ->
                    dependOnList.add(task)
                }
            }
            dependOnList.forEach {
                dependOnFunSpecBuilder.addStatement("list.add(%T::class.java)", ClassName(PACKAGE_NAME, it))
            }
            dependOnFunSpecBuilder
                .addStatement("return %L", "list")

            typeSpecBuilder.addFunction(dependOnFunSpecBuilder.build())
        }
        return typeSpecBuilder.build()
    }

    private fun test() {
        val dependOnList = arrayListOf<String>()
        dependOnArray.forEach { action ->
            taskActionMap[action]?.let { task ->
                dependOnList.add(task)
            }
        }

        println("===============")
        dependOnList.forEach {
            println("${element}的启动任务，依赖了${it}任务!")
        }
    }

    // 暂时先写这么多
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