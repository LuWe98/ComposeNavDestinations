package com.welu.composenavdestinations.extensions

inline fun <reified T> T.isAnyOf(vararg params: T): Boolean = params.any {
    this == it
}

inline fun <reified T> T.isNoneOf(vararg params: T): Boolean = params.none {
    this == it
}