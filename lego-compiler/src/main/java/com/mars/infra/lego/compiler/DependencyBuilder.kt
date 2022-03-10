package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.DependOn
import com.mars.infra.lego.compiler.ext.yes
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.lang.model.element.TypeElement

/**
 * Created by Mars on 2022/3/10
 */
class DependencyBuilder(element: TypeElement, taskActionMap: HashMap<String, String>) {

    private var dependOnArray: Array<String>
    private val dependOnList = arrayListOf<String>()

    init {
        // 例如：DependencyBuilder, element = com.mars.infra.lego.test.action2.FiveAction
        println("$TAG, element = $element")
        val annotation = element.getAnnotation(DependOn::class.java)
        dependOnArray = annotation.dependencies

        dependOnArray.forEach {
            // 例如：DependencyBuilder, dependOnArray = com.mars.infra.lego.test.action2.FourAction
            println("$TAG, dependOnArray = $it")
        }

        dependOnArray.isNotEmpty().yes {
            dependOnArray.forEach { action ->
                taskActionMap[action]?.let { task ->
                    dependOnList.add(task)
                }
            }
        }
    }

    private val dependOnFunSpecBuilder by lazy {
        FunSpec.builder("dependOn")
            .addModifiers(KModifier.OVERRIDE)
            .returns(listClassName.parameterizedBy(clzClassName.parameterizedBy(WildcardTypeName.producerOf(iTaskClassName.parameterizedBy(t)))))  // List<Class<out ITask<*>>>
            .addStatement("val list = %T()", arrayListClassName.parameterizedBy(clzClassName.parameterizedBy(WildcardTypeName.producerOf(iTaskClassName.parameterizedBy(t)))))
    }

    fun build(): FunSpec {
        dependOnList.forEach {
            dependOnFunSpecBuilder.addStatement("list.add(%T::class.java)", ClassName(TaskBuilder.PACKAGE_NAME, it))
        }
        dependOnFunSpecBuilder.addStatement("return %L", "list")
        return dependOnFunSpecBuilder.build()
    }

    companion object {
        const val TAG = "DependencyBuilder"
    }
}