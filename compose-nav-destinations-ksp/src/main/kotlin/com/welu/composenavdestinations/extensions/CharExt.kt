package com.welu.composenavdestinations.extensions

fun Char?.isWhitespace() = this == ' '

fun Char?.isLetterOrDigitOrUnderscore() = this?.isLetterOrDigit() == true || this == '_'
