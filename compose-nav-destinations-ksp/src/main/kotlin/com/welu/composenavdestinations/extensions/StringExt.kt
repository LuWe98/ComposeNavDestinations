package com.welu.composenavdestinations.extensions

inline fun String.ifNotBlank(defaultValue: (String) -> String): String = if (isNotBlank()) defaultValue(this) else this

fun CharSequence.indexOfFirst(char: Char): Int = indexOfFirst {
    it == char
}