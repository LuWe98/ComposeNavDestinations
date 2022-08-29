package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.extensions.ifNotBlank
import com.welu.composenavdestinations.model.ParameterTypeArgument.Star
import com.welu.composenavdestinations.model.ParameterTypeArgument.Typed

data class ParameterTypeInfo(
    val type: ParameterType,
    val isNullable: Boolean = false
) {

    val isEnum get() = type.isEnum
    val isParcelable get() = type.isParcelable
    val isSerializable get() = type.isSerializable
    val isKtxSerializable get() = type.isKtxSerializable
    val qualifiedName get() = type.import.qualifiedName
    val simpledName get() = type.import.simpleName

    val allChildImports
        get(): List<ImportInfo> = type.typeArguments.filterIsInstance<Typed>().flatMap(Typed::typeInfo / ParameterTypeInfo::allChildImports) + type.import

    val definition
        get(): String = run {
            if (type.typeArguments.isEmpty()) return@run type.import.simpleName

            type.import.simpleName + "<" + type.typeArguments.joinToString(", ") { type ->
                when (type) {
                    Star -> type.varianceLabel
                    is Typed -> type.varianceLabel.ifNotBlank { "$it " } + type.typeInfo.definition + if (type.typeInfo.isNullable) "?" else ""
                }
            } + ">"
        }
}