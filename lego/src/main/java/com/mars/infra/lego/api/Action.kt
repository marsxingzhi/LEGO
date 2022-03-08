package com.mars.infra.lego.api

/**
 * Created by Mars on 2022/3/8
 */
interface Action<T> {

    fun performAction(): T
}