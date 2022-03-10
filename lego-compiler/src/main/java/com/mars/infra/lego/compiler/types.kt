package com.mars.infra.lego.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeVariableName

/**
 * Created by Mars on 2022/3/10
 */
val abstractTaskClassName = ClassName("com.mars.infra.lego", "AbstractTask")
val listClassName = ClassName("kotlin.collections", "List")
val clzClassName = ClassName("java.lang", "Class")
val iTaskClassName = ClassName("com.mars.infra.lego.api", "ITask")
val arrayListClassName = ClassName("kotlin.collections", "ArrayList")
val t = TypeVariableName("*")