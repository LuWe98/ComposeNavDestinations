package com.welu.composenavdestinations.model.navargs

import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterTypeInfo

/**
 * @property typeInfo The NavArgs generic type info which is used in an ArgDestination
 * @property parameters Contains all primary constructor fields of the [typeInfo] property
 */
data class NavArgsInfo(
    val typeInfo: ParameterTypeInfo,
    val parameters: List<Parameter> = emptyList()
) {
    val name get() = typeInfo.simpledName
}