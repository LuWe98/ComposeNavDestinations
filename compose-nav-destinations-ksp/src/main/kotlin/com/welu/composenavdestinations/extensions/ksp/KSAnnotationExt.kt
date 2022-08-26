package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSValueArgument

fun KSAnnotation.getValueArgument(argName: String): KSValueArgument? = arguments.firstOrNull {
    it.name?.asString() == argName
}

fun KSAnnotation.requireValueArgument(argName: String): KSValueArgument =
    getValueArgument(argName) ?: throw IllegalStateException()
