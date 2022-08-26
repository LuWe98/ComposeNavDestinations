package com.welu.composenavdestinations.model

sealed interface ParameterTypeArgument {

    val varianceLabel: String

    data class Typed(
        val typeInfo: ParameterTypeInfo,
        override val varianceLabel: String = ""
    ): ParameterTypeArgument

    object Star: ParameterTypeArgument {
        override val varianceLabel = "*"
    }
}