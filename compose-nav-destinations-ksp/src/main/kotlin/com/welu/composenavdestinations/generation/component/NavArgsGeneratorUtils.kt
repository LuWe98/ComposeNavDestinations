package com.welu.composenavdestinations.generation.component

import com.welu.composenavdestinations.model.ArgContainer
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.components.NavComponentInfo

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
        val defaultValue = parameter.defaultValue?.let {

            //parameter.navArgTypeInfo.
//            val s = parameter.typeInfo.type.isArray
//            val b = parameter.typeInfo.type.typeArguments.first().typeInfo?.type
//            val ss = parameter.navArgTypeInfo.import
//
            "${it.value}, "
        } ?: ""
        "navArgument(\"${parameter.name}\", ${parameter.navArgTypeInfo.import.simpleName}, $defaultValue${parameter.typeInfo.isNullable})"
    }

    fun generateInvokeParameters(component: NavComponentInfo) = component
        .navArgsInfo!!
        .parameters
        .joinToString(",\n\t\t", transform = Parameter::fullDeclarationName)

    fun generateInvokeBody(routeName: String, sortedParams: List<Parameter>): String {
        var optionalNavSeparator = '?'
        return "\"$routeName\" +\n\t\t" + sortedParams.joinToString(" +\n\t\t") {
            val serializeSnipped = "\${${it.navArgTypeInfo.import.simpleName}.serializeValue(${it.name})}"
            if (it.typeInfo.isNullable || it.hasDefaultValue) {
                "\"$optionalNavSeparator${it.name}=$serializeSnipped\"".also {
                    optionalNavSeparator = '&'
                }
            } else {
                "\"/$serializeSnipped\""
            }
        }
    }

    fun generateGetArgsBody(component: NavComponentInfo, argContainer: ArgContainer): String = component
        .navArgsInfo!!
        .parameters
        .joinToString(",\n\t\t") {
            val nonNullableClaim = if (it.typeInfo.isNullable) "" else "!!"
            it.name + " = " + "${it.navArgTypeInfo.import.simpleName}.getTyped(${argContainer.variableName}, \"${it.name}\")$nonNullableClaim"
        }
}