package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.ifNotBlank

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


    val allImports
        get(): List<ImportInfo> = type.typeArguments.mapNotNull { it.typeInfo?.allImports }.flatten() + type.import

    val definition
        get(): String = run {
            if (type.typeArguments.isEmpty()) return@run type.import.simpleName

            type.import.simpleName + "<" + type.typeArguments.joinToString(", ") { type ->
                type.label.ifNotBlank { "$it " } + (type.typeInfo?.let { it.definition + if (it.isNullable) "?" else "" } ?: "")
            } + ">"
        }

}