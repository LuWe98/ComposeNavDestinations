package com.welu.composenavdestinations.navargs

//TODO -> Check if List Type is supported
sealed class ListNavArgType<T>(
    parseValueAction: (value: String) -> T?,
    serializeValueTransformation: (T) -> String = Any?::toString
) : CollectionNavArgType<T, List<T?>>(
    parseValueAction = parseValueAction,
    listToCollectionMapper = { it },
    serializeValueTransformation = serializeValueTransformation
)

/*
sealed class ListNavArgType<T>(
    private val parseValueAction: (value: String) -> T?,
    private val serializeValueTransformation: (T) -> String = Any?::toString,
    private val putAction: (bundle: Bundle, key: String, value: List<T?>?) -> Unit = Bundle::put
) : NavArgType<List<T?>?>() {

    override fun put(bundle: Bundle, key: String, value: List<T?>?) = putAction(bundle, key, value)

    override fun parseValue(value: String): List<T?>? = when (value) {
        DECODED_NULL_VALUE -> null
        "{}" -> emptyList()
        else -> value.subSequence(1, value.lastIndex).split(DECODED_VALUE_SEPARATOR).map {
            if (it == DECODED_NULL_VALUE) null else parseValueAction(it)
        }
    }

    override fun serializeValue(value: List<T?>?) = value?.let {
        "{" + value.joinToString(ENCODED_VALUE_SEPARATOR, transform = {
            it?.let(serializeValueTransformation) ?: ENCODED_NULL_VALUE
        }) + "}"
    } ?: ENCODED_NULL_VALUE

}
 */