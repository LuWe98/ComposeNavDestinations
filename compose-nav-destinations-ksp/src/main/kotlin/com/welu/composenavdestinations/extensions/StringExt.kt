package com.welu.composenavdestinations.extensions

inline fun String.ifNotBlank(defaultValue: (String) -> String): String = if (isNotBlank()) defaultValue(this) else this