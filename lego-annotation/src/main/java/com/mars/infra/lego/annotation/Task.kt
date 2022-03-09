package com.mars.infra.lego.annotation

/**
 * Created by Mars on 2022/3/8
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Task(
    val name: String,
    // val typeVariable: KClass<out Any>  // 不能写成KClass，apt过程哪有class概念？
    val typeVariable: String,
    val dependOn: Array<String> = [] // TODO 暂时写在这里，后续考虑是否单独拎出来作为一个注解；这个也用字符串表示，感觉太麻烦了，容易写错
)