package com.welu.composenavdestinations.util

import android.net.Uri
import kotlin.reflect.KClass

object Constants {

    inline fun <reified T> Any?.toActualValue(): T? = if(this is T) this else null

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> Any?.toActualValue(clazz: KClass<T>): T? = if(this == null) null else if(this::class == clazz) this as T? else null

}