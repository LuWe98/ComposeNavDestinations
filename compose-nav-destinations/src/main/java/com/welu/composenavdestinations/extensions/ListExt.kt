package com.welu.composenavdestinations.extensions

import kotlin.reflect.KClass

val <T> List<T>.asArrayList get() = ArrayList(this)

@Suppress("UNCHECKED_CAST")
fun <T: Any> Collection<T?>.toTypedArray(type: KClass<T>): Array<T?> {
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    return (this as java.util.Collection<T>).toArray(java.lang.reflect.Array.newInstance(type.java, 0) as Array<T?>) as Array<T?>
}

@Suppress("UNCHECKED_CAST")
fun<T: Any> emptyArrayReflect(type: KClass<T>): Array<T?> {
    return java.lang.reflect.Array.newInstance(type.java, 0) as Array<T?>
}