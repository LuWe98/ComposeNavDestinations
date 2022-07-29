package com.welu.composenavdestinations.extensions

fun Char?.isWhitespace() = this == ' '

fun Char.isDot() = this == '.'

fun Char.isLetterDigitOrUnderscore() = isLetterOrDigit() || this == '_'

