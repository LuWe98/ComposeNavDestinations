package com.welu.composenavdestinations.extensions

import kotlin.reflect.KProperty1

inline operator fun <reified A, reified B, reified C> KProperty1<A, B>.div(crossinline getter: (B) -> C): (A) -> C = {
    getter(this(it))
}