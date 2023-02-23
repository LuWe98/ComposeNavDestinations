package com.welu.compose_nav_destinations_ksp.generation.component

import com.welu.compose_nav_destinations_ksp.model.AndroidArgsContainer
import com.welu.compose_nav_destinations_ksp.model.Parameter
import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.actualTypeName

object NavArgsGeneratorUtils {

    fun generateRoute(sortedParams: List<Parameter>): String {
        var optionalNavSeparator = '?'
        return sortedParams.joinToString(" +\n\t\t") {
            if (it.typeInfo.isNullable || it.hasDefaultValue) {
                "\"$optionalNavSeparator${it.name}={${it.name}}\"".also {
                    optionalNavSeparator = '&'
                }
            } else {
                "\"/{${it.name}}\""
            }
        }
    }

    fun generateNamedNavArguments(sortedParams: List<Parameter>) = sortedParams.joinToString(",\n\t\t") { parameter ->
        val defaultValue = parameter.defaultValue?.let { "${it.value}, " } ?: ""
        "navArgument(\"${parameter.name}\", ${parameter.navArgType.actualTypeName}, $defaultValue${parameter.typeInfo.isNullable})"
    }

    fun generateInvokeParameters(component: NavComponentInfo) = component
        .navArgsInfo!!
        .parameters
        .joinToString(",\n\t\t", transform = Parameter::fullDeclarationName)

    fun generateInvokeBody(routeName: String, sortedParams: List<Parameter>): String {
        var optionalNavSeparator = '?'
        return "\"$routeName\" +\n\t\t" + sortedParams.joinToString(" +\n\t\t") {
            val serializeSnipped = "\${${it.navArgType.actualTypeName}.serializeValue(${it.name})}"
            if (it.typeInfo.isNullable || it.hasDefaultValue) {
                "\"$optionalNavSeparator${it.name}=$serializeSnipped\"".also {
                    optionalNavSeparator = '&'
                }
            } else {
                "\"/$serializeSnipped\""
            }
        }
    }

    fun generateGetArgsBody(component: NavComponentInfo, argContainer: AndroidArgsContainer): String = component
        .navArgsInfo!!
        .parameters
        .joinToString(",\n\t\t") { parameter ->
            val nonNullableClaim = if (parameter.typeInfo.isNullable) "" else "!!"
            parameter.name + " = " + "${parameter.navArgType.actualTypeName}.getTyped(${argContainer.variableName}, \"${parameter.name}\")$nonNullableClaim"
        }
}