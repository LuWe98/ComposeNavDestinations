package com.welu.compose_nav_destinations_ksp.model

data class ParameterType(
    val import: ImportInfo,
    val isEnum: Boolean = false,
    val isSerializable: Boolean = false,
    val isKotlinSerializable: Boolean = false,
    val isParcelable: Boolean = false,
    val isList: Boolean = false,
    val isSet: Boolean = false,
    val typeArguments: List<ParameterTypeArgument> = emptyList()
) {

    val isArray get() = import.qualifiedName == Array::class.qualifiedName

}