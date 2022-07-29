package com.welu.composenavdestinations.extensions

@Suppress("SameParameterValue")
inline fun <reified T> T.isOneOf(vararg params: T): Boolean = params.any {
    this == it
}

@Suppress("SameParameterValue")
inline fun <reified T> T.isNoneOf(vararg params: T): Boolean = params.none {
    this == it
}