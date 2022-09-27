package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.flattenMutable

data class Parameter(
    val name: String,
    val typeInfo: ParameterTypeInfo,
    //TODO -> Austauschen zu NavArgType
    val navArgTypeInfo: ParameterNavTypeInfo,
    val defaultValue: ParameterDefaultValue? = null
) {

    val fullDeclarationName get() = "$name: ${typeInfo.definition}" + (defaultValue?.let { " = " + it.value } ?: "")
    val hasDefaultValue get() = defaultValue != null
    val hasCustomNavArgType get() = navArgTypeInfo.navArgType is CustomNavArgType

    val imports
        get(): List<ImportInfo> = typeInfo.type.typeArguments
            .mapNotNull { it.typeInfo?.allImports }
            .flattenMutable().apply {
                add(typeInfo.type.import)
                add(navArgTypeInfo.import)
                defaultValue?.requiredImports?.let(::addAll)
            }.distinctBy(ImportInfo::qualifiedName)

}