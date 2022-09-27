package com.welu.composenavdestinations.model

import com.welu.composenavdestinations.extensions.flattenMutable
import com.welu.composenavdestinations.model.navargs.ComplexParameterNavArgType
import com.welu.composenavdestinations.model.navargs.ParameterNavArgType
import com.welu.composenavdestinations.model.navargs.actualTypeImport

/**
 * Represents a parameter inside the NavArgs of a NavComponentSpec
 *
 *      data class CustomNavArgs(param1: String, param2: String)
 *      -> param1 is the first Parameter
 *      -> param2 is the second Parameter of the wrapping CustomNavArgs class
 *
 * @property name The name of this [Parameter]
 * @property typeInfo The [ParameterTypeInfo] of this [Parameter]. Contains Information about the types of this [Parameter]
 * @property navArgType The [ParameterNavArgType] that should be used to parse and serialize this [Parameter]. Can either be a [BasicNavArgType] or a [ComplexParameterNavArgType].
 * @property defaultValue Represents the default value of this [Parameter]
 */
data class Parameter(
    val name: String,
    val typeInfo: ParameterTypeInfo,
    val navArgType: ParameterNavArgType,
    val defaultValue: ParameterDefaultValue? = null
) {

    /**
     * The full declaration of this [Parameter]. E.g:
     *
     *     param: String = "paramValue"
     */
    val fullDeclarationName get() = "$name: ${typeInfo.definition}" + (defaultValue?.let { " = " + it.value } ?: "")
    val hasDefaultValue get() = defaultValue != null
    val hasComplexNavArgType get() = navArgType is ComplexParameterNavArgType

    val imports
        get(): List<ImportInfo> = typeInfo.type.typeArguments
            .mapNotNull { it.typeInfo?.allImports }
            .flattenMutable().apply {
                add(typeInfo.type.import)
                add(navArgType.actualTypeImport)
                defaultValue?.requiredImports?.let(::addAll)
            }.distinctBy(ImportInfo::qualifiedName)

}