package com.welu.compose_nav_destinations_ksp.model

import com.welu.compose_nav_destinations_ksp.model.navargs.ComplexParameterNavArgType
import com.welu.compose_nav_destinations_ksp.model.navargs.ParameterNavArgType
import com.welu.compose_nav_destinations_ksp.model.navargs.actualTypeImport

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

    val typeImports get(): Set<ImportInfo> = mutableSetOf(typeInfo.type.import).apply {
        addAll(typeInfo.type.typeArguments.mapNotNull { it.typeInfo?.imports }.flatten())
        defaultValue?.requiredImports?.let(::addAll)
    }

    val imports get(): Set<ImportInfo> = typeImports + navArgType.actualTypeImport

}