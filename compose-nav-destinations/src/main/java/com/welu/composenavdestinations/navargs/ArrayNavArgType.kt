package com.welu.composenavdestinations.navargs

import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_VALUE_SEPARATOR
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_VALUE_SEPARATOR

sealed class ArrayNavArgType<T : Any, Entry : Any>(
    private val emptyArrayProvider: () -> T,
    private val parseValueAction: (value: String) -> Entry,
    private val arrayProvider: (list: List<Entry>) -> T,
    private val arrayToStringTransformation: (T, separator: String) -> String
) : NavArgType<T?>() {

    override fun parseValue(value: String): T? = when (value) {
        DECODED_NULL_VALUE -> null
        "[]" -> emptyArrayProvider()
        else -> value.subSequence(1, value.lastIndex).split(DECODED_VALUE_SEPARATOR).map(parseValueAction).let {
            arrayProvider(it)
        }
    }

    override fun serializeValue(value: T?) = value?.let {
        "[" + arrayToStringTransformation(it, ENCODED_VALUE_SEPARATOR) + "]"
    } ?: ENCODED_NULL_VALUE

}