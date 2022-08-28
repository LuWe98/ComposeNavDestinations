package com.welu.composenavdestinations.navargs

import com.welu.composenavdestinations.extensions.emptyArrayReflect
import com.welu.composenavdestinations.extensions.toTypedArray
import kotlin.reflect.KClass
import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_VALUE_SEPARATOR
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_VALUE_SEPARATOR

sealed class ArrayNavArgType<T : Any>(
    private val parseValueAction: (value: String) -> T?,
    private val serializeValueTransformation: (T) -> String = Any?::toString,
    private val clazz: KClass<T>? = null,
    private val emptyArrayProvider: () -> Array<T?> = { emptyArrayReflect(clazz!!) },
    private val arrayProvider: (list: List<T?>) -> Array<T?> = { it.toTypedArray(clazz!!) }
) : NavArgType<Array<T?>?>() {

    override fun parseValue(value: String): Array<T?>? = when (value) {
        DECODED_NULL_VALUE -> null
        "[]" -> emptyArrayProvider()
        else -> value.subSequence(1, value.lastIndex).split(DECODED_VALUE_SEPARATOR).map {
            if (it == DECODED_NULL_VALUE) null else parseValueAction(it)
        }.let(arrayProvider)
    }

    @JvmName("serializeValue2")
    @Suppress("UNCHECKED_CAST")
    fun serializeValue(value: Array<T>?) = serializeValue(value as Array<T?>?)

    override fun serializeValue(value: Array<T?>?) = value?.let {
        "[" + value.joinToString(ENCODED_VALUE_SEPARATOR) {
            it?.let(serializeValueTransformation) ?: ENCODED_NULL_VALUE
        } + "]"
    } ?: ENCODED_NULL_VALUE

}