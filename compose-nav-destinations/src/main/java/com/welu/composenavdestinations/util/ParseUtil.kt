package com.welu.composenavdestinations.util

import androidx.navigation.NavType

object ParseUtil {

    //TODO -> vllt noch weiterschauen
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> parseValue(value: String): T = when(T::class){
        Short::class -> parseShort(value)
        Int::class -> NavType.IntType.parseValue(value)
        Long::class -> NavType.LongType.parseValue(value)
        Boolean::class -> NavType.BoolType.parseValue(value)
        String::class -> NavType.StringType.parseValue(value)
        Float::class -> NavType.FloatType.parseValue(value)
        else -> when {
            else -> throw IllegalStateException("Cannot resolve type: ${T::class.qualifiedName}")
        }
    } as T

    fun parseShort(value: String): Short = if (value.startsWith("0x")) {
        value.substring(2).toShort(16)
    } else {
        value.toShort()
    }

    fun parseByte(value: String): Byte = if (value.startsWith("0x")) {
        value.substring(2).toByte(16)
    } else {
        value.toByte()
    }

    fun parseDouble(value: String): Double = value.toDouble()

    fun parseChar(value: String): Char = value.first()

 }