package com.mars.infra.lego.annotation

/**
 * Created by Mars on 2022/3/10
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class DependOn(
    val dependencies: Array<String>
)