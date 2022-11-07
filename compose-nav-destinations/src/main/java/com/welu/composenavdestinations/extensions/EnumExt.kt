package com.welu.composenavdestinations.extensions

import kotlin.reflect.KClass

internal fun <T: Enum<T>> KClass<T>.findValueOf(name: String): T = java.enumConstants?.firstOrNull { enumEntry ->
    enumEntry.name == name
} ?: throw IllegalStateException("Could not find Enum constant of class: ${this.qualifiedName} with the specified name: $name")


//internal inline fun <reified T: Enum<T>> KClass<T>.findValueOfInline(name: String): T = java.enumConstants?.firstOrNull { enumEntry ->
//    enumEntry.name == name
//} ?: throw IllegalStateException()