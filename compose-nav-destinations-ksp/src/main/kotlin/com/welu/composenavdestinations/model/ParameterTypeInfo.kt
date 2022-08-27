package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.model.ParameterTypeArgument.*

data class ParameterTypeInfo(
    val type: ParameterType,
    val isNullable: Boolean
) {

    val allChildImports get(): List<PackageImportInfo> = type.typeArguments.filterIsInstance<Typed>().flatMap {
        it.typeInfo.allChildImports
    } + type.import

    val isEnum get() = type.isEnum
    val isParcelable get() = type.isParcelable
    val isSerializable get() = type.isSerializable
    val isKtxSerializable get() = type.isKtxSerializable
    val qualifiedName get() = type.import.qualifiedName
    val hasNoTypeArguments get() = type.typeArguments.isEmpty()
    val hasOneTypeArgument get() = type.typeArguments.size == 1

    val definition get(): String = run {
        if(type.typeArguments.isEmpty()) return@run type.import.simpleName

        type.import.simpleName+ "<" + type.typeArguments.joinToString(", ") {
            when(it) {
                Star -> it.varianceLabel
                is Typed -> (if(it.varianceLabel.isBlank()) "" else it.varianceLabel + " ") + it.typeInfo.definition + if(it.typeInfo.isNullable) "?" else ""
            }
        } + ">"
    }

}