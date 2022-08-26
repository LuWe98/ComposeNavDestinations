package com.welu.composenavdestinations.navargs

import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_NULL_VALUE

sealed class SingleNavArgType<T : Any>(
    private val parseValueAction: (value: String) -> T,
    private val serializeValueAction: (T) -> String = Any::toString
) : NavArgType<T?>() {

    override fun parseValue(value: String): T? = if (value == DECODED_NULL_VALUE) null else parseValueAction(value)

    override fun serializeValue(value: T?): String = value?.let(serializeValueAction) ?: ENCODED_NULL_VALUE

}