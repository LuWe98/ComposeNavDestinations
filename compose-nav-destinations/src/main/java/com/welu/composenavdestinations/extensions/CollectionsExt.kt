package com.welu.composenavdestinations.extensions

import kotlin.reflect.KClass
import java.lang.reflect.Array as JavaReflectArray

val <T> List<T>.asArrayList get(): ArrayList<T>  = if(this is ArrayList) this else ArrayList(this)

val <T> Set<T>.asHashSet get(): HashSet<T> = if(this is HashSet) this else HashSet(this)

@Suppress("UNCHECKED_CAST")
fun <T: Any> Collection<T?>.toTypedArray(type: KClass<T>): Array<T?> {
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    return (this as java.util.Collection<T>).toArray(JavaReflectArray.newInstance(type.java, 0) as Array<T?>) as Array<T?>
}

@Suppress("UNCHECKED_CAST")
fun <T: Any> emptyArray(clazz: KClass<T>): Array<T?> = JavaReflectArray.newInstance(clazz.java, 0) as Array<T?>