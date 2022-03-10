package com.mars.infra.lego.compiler

import com.mars.infra.lego.annotation.BlockMainThread
import com.mars.infra.lego.annotation.DependOn
import com.mars.infra.lego.annotation.Task
import com.mars.infra.lego.annotation.WorkerThread
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
        val taskActionMap = hashMapOf<String, String>()
        val taskMap = hashMapOf<Element, TaskBuilder>()
        val dependencyMap = hashMapOf<Element, DependencyBuilder>()
        val workerThreadMap = hashMapOf<Element, WorkerThreadBuilder>()
        val blockMainThreadMap = hashMapOf<Element, BlockMainThreadBuilder>()

        env.getElementsAnnotatedWith(Task::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                taskMap[element] = TaskBuilder(element, taskActionMap)
            }

        env.getElementsAnnotatedWith(DependOn::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                dependencyMap[element] =  DependencyBuilder(element, taskActionMap)
            }

        env.getElementsAnnotatedWith(WorkerThread::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                workerThreadMap[element] = WorkerThreadBuilder(element)
            }

        env.getElementsAnnotatedWith(BlockMainThread::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                blockMainThreadMap[element] = BlockMainThreadBuilder(element)
            }

        taskActionMap.entries.forEach { entry ->
            println("action = ${entry.key}, generateTask = ${entry.value}")
        }

        taskMap.entries.forEach { entry ->
            println("LegoProcessor#taskMap, element = ${entry.key}, taskBuilder = ${entry.value}")

            val dependOnBuilder = dependencyMap[entry.key]
            println("LegoProcessor#dependencyMap, element = ${entry.key}, dependOnBuilder = $dependOnBuilder")

            val workerThreadBuilder = workerThreadMap[entry.key]
            println("LegoProcessor#workerThreadMap, element = ${entry.key}, workerThreadBuilder = $workerThreadBuilder")

            val blockMainThreadBuilder = blockMainThreadMap[entry.key]
            println("LegoProcessor#blockMainThreadMap, element = ${entry.key}, blockMainThreadBuilder = $blockMainThreadBuilder")

            entry.value.apply {
                setBuilders(dependOnBuilder, workerThreadBuilder, blockMainThreadBuilder)
                build(ProcessorManager.filer)
            }
        }
        return true
    }
}