package com.welu.compose_nav_destinations_ksp.extensions

fun <T> Iterable<Iterable<T>>.flattenMutable(): MutableList<T> {
    val result = ArrayList<T>()
    for (element in this) {
        result.addAll(element)
    }
    return result
}