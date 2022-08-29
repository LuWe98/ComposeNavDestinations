package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.model.ParameterTypeArgument.*
import com.welu.composenavdestinations.utils.PackageUtils

data class Parameter(
    val name: String,
    val typeInfo: ParameterTypeInfo,
    //Das ist der NavArgImport Name -> bsp. NavArgByteSetType
    val navArgInfo: ParameterNavArgInfo,
    val defaultValue: DefaultValue? = null
) {

    val hasDefaultValue get() = defaultValue != null
    val fullTypeName get() = typeInfo.definition
    val fullName get() = "$name: $fullTypeName"
    val fullDeclarationName get() = fullName + (defaultValue?.let { " = " + it.value } ?: "")
    val isCustomNavArgType get() = navArgInfo.customNavArgInfo != null

    val imports
        get(): List<ImportInfo> = typeInfo.type.typeArguments
            .filterIsInstance<Typed>()
            .flatMap(Typed::typeInfo / ParameterTypeInfo::allChildImports)
            .toMutableList().apply {
                add(typeInfo.type.import)
                add(navArgInfo.import)
                defaultValue?.requiredImports?.let(::addAll)
            }.distinctBy(ImportInfo::qualifiedName)

}