package com.welu.composenavdestinations.util

object ParseUtil {

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