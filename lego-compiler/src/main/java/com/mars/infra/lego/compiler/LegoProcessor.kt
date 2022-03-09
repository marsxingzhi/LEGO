package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.Task
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * Created by Mars on 2022/3/8
 */
class LegoProcessor : AbstractProcessor() {

    private val annotations = setOf(Task::class.java)

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }


    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return annotations.mapTo(HashSet(), Class<*>::getCanonicalName)
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        ProcessorManager.init(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        env: RoundEnvironment?
    ): Boolean {
        env ?: return false
        val taskMap = hashMapOf<Element, TaskBuilder>()
        val taskActionMap = hashMapOf<String, String>()
        env.getElementsAnnotatedWith(Task::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                // LegoProcessor---element = com.mars.infra.lego.test.action2.FirstAction
                println("LegoProcessor---element = $element")
                taskMap[element] = TaskBuilder(element, taskActionMap)
            }
        taskActionMap.entries.forEach { entry ->
            println("action = ${entry.key}, generateTask = ${entry.value}")
        }
        taskMap.values.forEach { taskBuilder ->
            taskBuilder.build(ProcessorManager.filer)
        }
        return true
    }
}