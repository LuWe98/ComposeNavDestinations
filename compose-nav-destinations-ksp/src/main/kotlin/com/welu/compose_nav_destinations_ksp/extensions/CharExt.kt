package com.welu.compose_nav_destinations_ksp.extensions

fun Char?.isWhitespace() = this == ' '

fun Char?.isLetterOrDigitOrUnderscore() = this?.isLetterOrDigit() == true || this == '_'
