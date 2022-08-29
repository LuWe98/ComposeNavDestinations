package com.welu.composenavdestinations.model

data class ParameterType(
    val import: ImportInfo,
    val isEnum: Boolean = false,
    val isSerializable: Boolean = false,
    val isKtxSerializable: Boolean = false,
    val isParcelable: Boolean = false,
    val isList: Boolean = false,
    val isSet: Boolean = false,
    val typeArguments: List<ParameterTypeArgument> = emptyList()
) {

    val isArray get() = import.qualifiedName == Array::class.qualifiedName

}