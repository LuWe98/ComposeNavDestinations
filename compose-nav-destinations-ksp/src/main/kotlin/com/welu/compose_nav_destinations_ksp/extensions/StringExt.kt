package com.welu.compose_nav_destinations_ksp.extensions

inline fun String.ifNotBlank(defaultValue: (String) -> String): String = if (isNotBlank()) defaultValue(this) else this

fun CharSequence.indexOfFirst(char: Char): Int = indexOfFirst {
    it == char
}