package com.welu.compose_nav_destinations_ksp.extensions

import kotlin.reflect.KProperty1

inline operator fun <reified A, reified B, reified C> KProperty1<A, B>.div(crossinline getter: (B) -> C): (A) -> C = {
    getter(this(it))
}