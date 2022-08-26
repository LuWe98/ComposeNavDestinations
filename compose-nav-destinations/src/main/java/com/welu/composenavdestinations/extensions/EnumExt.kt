package com.welu.composenavdestinations.extensions

import kotlin.reflect.KClass

internal fun <T: Enum<T>> KClass<T>.receiveValueOf(name: String): T = java.enumConstants?.firstOrNull { enumEntry ->
    enumEntry.name == name
} ?: throw IllegalStateException()


internal inline fun <reified T: Enum<T>> KClass<T>.receiveValueOf2(name: String): T = java.enumConstants?.firstOrNull { enumEntry ->
    enumEntry.name == name
} ?: throw IllegalStateException()

