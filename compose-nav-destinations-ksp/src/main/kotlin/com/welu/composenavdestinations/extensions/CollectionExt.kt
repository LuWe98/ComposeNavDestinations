package com.welu.composenavdestinations.extensions

fun <T> Iterable<Iterable<T>>.flattenMutable(): MutableList<T> {
    val result = ArrayList<T>()
    for (element in this) {
        result.addAll(element)
    }
    return result
}