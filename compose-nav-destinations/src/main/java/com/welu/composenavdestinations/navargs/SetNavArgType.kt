package com.welu.composenavdestinations.navargs

//TODO -> Check if Set Type is supported
sealed class SetNavArgType<T>(
    parseValueAction: (value: String) -> T?,
    serializeValueTransformation: (T) -> String = Any?::toString
) : CollectionNavArgType<T, Set<T?>>(
    parseValueAction = parseValueAction,
    listToCollectionMapper = List<T?>::toSet,
    serializeValueTransformation = serializeValueTransformation
)

/*
sealed class SetNavArgType<T>(
    private val parseValueAction: (value: String) -> T?,
    private val serializeValueTransformation: (T) -> String = Any?::toString,
    private val putAction: (bundle: Bundle, key: String, value: Set<T?>?) -> Unit = Bundle::put
) : NavArgType<Set<T?>?>() {

    override fun put(bundle: Bundle, key: String, value: Set<T?>?) = putAction(bundle, key, value)

    override fun parseValue(value: String): Set<T?>? = when (value) {
        NavArgConstants.DECODED_NULL_VALUE -> null
        "{}" -> emptySet()
        else -> value.subSequence(1, value.lastIndex).split(NavArgConstants.DECODED_VALUE_SEPARATOR).map {
            if (it == NavArgConstants.DECODED_NULL_VALUE) null else parseValueAction(it)
        }.toSet()
    }

    override fun serializeValue(value: Set<T?>?) = value?.let {
        "{" + value.joinToString(NavArgConstants.ENCODED_VALUE_SEPARATOR, transform = {
            it?.let(serializeValueTransformation) ?: NavArgConstants.ENCODED_NULL_VALUE
        }) + "}"
    } ?: NavArgConstants.ENCODED_NULL_VALUE

}
 */
