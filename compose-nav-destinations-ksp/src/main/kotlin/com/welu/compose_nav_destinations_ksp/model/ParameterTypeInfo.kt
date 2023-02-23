package com.welu.compose_nav_destinations_ksp.model

import com.welu.compose_nav_destinations_ksp.extensions.ifNotBlank

data class ParameterTypeInfo(
    val type: ParameterType,
    val isNullable: Boolean = false
) {

    val isEnum get() = type.isEnum
    val isParcelable get() = type.isParcelable
    val isSerializable get() = type.isSerializable
    val isKotlinSerializable get() = type.isKotlinSerializable
    val qualifiedName get() = type.import.qualifiedName
    val simpledName get() = type.import.simpleName


    val imports get(): List<ImportInfo> = type.typeArguments.mapNotNull { it.typeInfo?.imports }.flatten() + type.import

    val definition get(): String = if (type.typeArguments.isEmpty()) {
        type.import.simpleName
    } else {
        type.import.simpleName + "<" + type.typeArguments.joinToString(", ") { type ->
            type.label.ifNotBlank { "$it " } + (type.typeInfo?.definition ?: "")
        } + ">"
    } + if (isNullable) "?" else ""

}