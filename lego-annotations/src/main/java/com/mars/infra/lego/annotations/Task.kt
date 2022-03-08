package com.mars.infra.lego.annotations

/**
 * Created by Mars on 2022/3/8
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Task(
    val name: String
)
