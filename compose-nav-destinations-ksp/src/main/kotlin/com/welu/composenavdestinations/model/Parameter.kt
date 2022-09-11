package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.flattenMutable

data class Parameter(
    val name: String,
    val typeInfo: ParameterTypeInfo,
    //Das ist der NavArgImport Name -> bsp. NavArgByteSetType
    val navArgInfo: ParameterNavArgInfo,
    val defaultValue: ParameterDefaultValue? = null
) {

    val hasDefaultValue get() = defaultValue != null
    val fullTypeName get() = typeInfo.definition
    val fullName get() = "$name: $fullTypeName"
    val fullDeclarationName get() = fullName + (defaultValue?.let { " = " + it.value } ?: "")
    val isCustomNavArgType get() = navArgInfo.customNavArgInfo != null

    val imports
        get(): List<ImportInfo> = typeInfo.type.typeArguments
            .mapNotNull { it.typeInfo?.allChildImports }
            .flattenMutable().apply {
                add(typeInfo.type.import)
                add(navArgInfo.import)
                defaultValue?.requiredImports?.let(::addAll)
            }.distinctBy(ImportInfo::qualifiedName)

}