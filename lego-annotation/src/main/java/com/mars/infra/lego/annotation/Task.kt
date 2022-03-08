package com.mars.infra.lego.annotation

/**
 * Created by Mars on 2022/3/8
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Task(
    val name: String,
    // val typeVariable: KClass<out Any>  // 不能写成KClass，apt过程哪有class概念？
    val typeVariable: String
)
