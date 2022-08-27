package com.welu.composenavdestinations.navargs

import android.os.Bundle
import com.welu.composenavdestinations.extensions.put
import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.DECODED_VALUE_SEPARATOR
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_NULL_VALUE
import com.welu.composenavdestinations.navargs.NavArgConstants.ENCODED_VALUE_SEPARATOR

//TODO -> Check if List Type is supported
sealed class CollectionNavArgType<E, T : Collection<E?>>(
    private val parseValueAction: (value: String) -> E?,
    private val collectionProvider: () -> MutableCollection<E?>,
    private val serializeValueTransformation: (E) -> String = Any?::toString
) : NavArgType<T?>() {

    override fun put(bundle: Bundle, key: String, value: T?) = bundle.put(key, value)

    @Suppress("UNCHECKED_CAST")
    override fun parseValue(value: String): T? = when (value) {
        DECODED_NULL_VALUE -> null
        "{}" -> collectionProvider()
        else -> value.subSequence(1, value.lastIndex).split(DECODED_VALUE_SEPARATOR).mapTo(collectionProvider()) {
            if (it == DECODED_NULL_VALUE) null else parseValueAction(it)
        }
    } as T?

    override fun serializeValue(value: T?) = value?.let {
        "{" + value.joinToString(ENCODED_VALUE_SEPARATOR, transform = {
            it?.let(serializeValueTransformation) ?: ENCODED_NULL_VALUE
        }) + "}"
    } ?: ENCODED_NULL_VALUE

}