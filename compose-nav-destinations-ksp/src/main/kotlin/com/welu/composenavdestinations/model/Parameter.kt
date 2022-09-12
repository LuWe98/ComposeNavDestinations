package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.flattenMutable

data class Parameter(
    val name: String,
    val typeInfo: ParameterTypeInfo,
    val navArgTypeInfo: ParameterNavTypeInfo,
    val defaultValue: ParameterDefaultValue? = null
) {

    val fullDeclarationName get() = "$name: ${typeInfo.definition}" + (defaultValue?.let { " = " + it.value } ?: "")
    val hasDefaultValue get() = defaultValue != null
    val hasCustomNavArgType get() = navArgTypeInfo.customNavTypeInfo != null

    val imports
        get(): List<ImportInfo> = typeInfo.type.typeArguments
            .mapNotNull { it.typeInfo?.allChildImports }
            .flattenMutable().apply {
                add(typeInfo.type.import)
                add(navArgTypeInfo.import)
                defaultValue?.requiredImports?.let(::addAll)
            }.distinctBy(ImportInfo::qualifiedName)

}