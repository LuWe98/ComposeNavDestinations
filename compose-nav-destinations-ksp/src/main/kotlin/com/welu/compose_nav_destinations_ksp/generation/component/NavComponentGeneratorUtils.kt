package com.welu.compose_nav_destinations_ksp.generation.component

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier.OPERATOR
import com.squareup.kotlinpoet.KModifier.OVERRIDE
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.build
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.getter
import com.welu.compose_nav_destinations_ksp.extensions.kotlinpoet.parameterizedBy
import com.welu.compose_nav_destinations_ksp.extensions.toClassName
import com.welu.compose_nav_destinations_ksp.model.ArgumentContainer
import com.welu.compose_nav_destinations_ksp.model.ImportInfo
import com.welu.compose_nav_destinations_ksp.model.Parameter
import com.welu.compose_nav_destinations_ksp.model.components.NavComponentInfo
import com.welu.compose_nav_destinations_ksp.model.navargs.actualTypeSimpleName
import com.welu.compose_nav_destinations_ksp.utils.ImportUtils

object NavComponentGeneratorUtils {

    fun generateParentNavGraphProperty(parentNavGraphSpecImport: ImportInfo) = PropertySpec.build(
        name = "parentNavGraphSpec",
        typeName = parentNavGraphSpecImport.toClassName(),
        modifiers = arrayOf(OVERRIDE)
    ) {
        getter {
            addStatement("return %T", parentNavGraphSpecImport.toClassName())
        }
    }

    fun generateBaseRouteProperty(
        baseRoute: String
    ) = PropertySpec.build(
        name = "baseRoute",
        typeName = STRING,
        modifiers = arrayOf(OVERRIDE)
    ) {
        initializer("\"$baseRoute\"")
    }

    fun generateArgRouteProperty(
        requiredParameters: List<Parameter>,
        optionalParameters: List<Parameter>
    ) = PropertySpec.build(
        name = "route",
        typeName = STRING,
        modifiers = arrayOf(OVERRIDE)
    ) {
        initializer("baseRoute + \n" + generateRoutePropertyBody(requiredParameters, optionalParameters))
    }

    private fun generateRoutePropertyBody(
        requiredParameters: List<Parameter>,
        optionalParameters: List<Parameter>
    ): String {
        val body = requiredParameters.joinToString(" +\n") { "\"/{${it.name}}\"" }

        if(optionalParameters.none()) return body

        return "$body+\n\"?" + optionalParameters.joinToString(" +\n\"&") {
            "${it.name}={${it.name}}\""
        }
    }

    fun generateNavArgumentsProperty(
        requiredParameters: List<Parameter>,
        optionalParameters: List<Parameter>
    ) = PropertySpec.build(
        name = "arguments",
        typeName = LIST.parameterizedBy(ImportUtils.ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT),
        modifiers = arrayOf(OVERRIDE)
    ) {
        initializer("listOf(\n${generateNavArgumentsPropertyBody(requiredParameters + optionalParameters)}\n)")
    }


    //TODO -> Hier vllt noch ändern, dass man immer in ne neue Zeile schreibt
    private fun generateNavArgumentsPropertyBody(
        orderedParameters: List<Parameter>
    ) = orderedParameters.joinToString(",\n") { parameter ->
        val defaultValue = parameter.defaultValue?.let { ",\n\tdefaultValue = ${it.value}" } ?: ""
        "navArgument(\n\tname = \"${parameter.name}\",\n\ttype = ${parameter.navArgType.actualTypeSimpleName},\n\tnullable = ${parameter.typeInfo.isNullable}$defaultValue\n)"
    }

    fun generateGetArgsFromFunction(
        component: NavComponentInfo,
        containerType: ArgumentContainer
    ) = FunSpec.build("argsFrom") {
        val navArgsType = component.navArgsInfo!!.typeInfo.type

        addParameter(containerType.variableName, containerType.import.toClassName())
        addModifiers(OVERRIDE)
        returns(navArgsType.import.toClassName())

        val argsFromBody = generateGetArgsFromFunctionBody(component, containerType)
        addStatement("return ${navArgsType.import.simpleName}(\n\t\t$argsFromBody\n)")
    }

    private fun generateGetArgsFromFunctionBody(
        component: NavComponentInfo,
        containerType: ArgumentContainer
    ): String = component
        .navArgsInfo!!
        .parameters
        .joinToString(",\n\t\t") { parameter ->
            val nonNullableClaim = if (parameter.typeInfo.isNullable) "" else "!!"
            parameter.name + " = " + "${parameter.navArgType.actualTypeSimpleName}.getTyped(${containerType.variableName}, \"${parameter.name}\")$nonNullableClaim"
        }

    fun generateInvokeFunction(
        baseRoute: String,
        requiredParameters: List<Parameter>,
        optionalParameters: List<Parameter>
    ) = FunSpec.build("invoke") {
        addModifiers(OPERATOR)
        returns(ImportUtils.ROUTABLE_IMPORT.toClassName())

        generateInvokeParameters(requiredParameters).forEach(::addParameter)
        generateInvokeParameters(optionalParameters).forEach(::addParameter)

        val invokeBody = generateInvokeBody(baseRoute, requiredParameters, optionalParameters)
        addStatement("return Routable.of(\n\t$invokeBody\n)")
    }

    private fun generateInvokeParameters(
        orderedParameters: List<Parameter>
    ): List<ParameterSpec> = orderedParameters.map {
        ParameterSpec.build(it.name, it.typeInfo.toParameterizedTypeName()) {
            it.defaultValue?.value?.let(::defaultValue)
        }
    }

    private fun generateInvokeBody(
        baseRoute: String,
        requiredParameters: List<Parameter>,
        optionalParameters: List<Parameter>
    ): String {
        val body = "\"$baseRoute\" +\n\t" + requiredParameters.joinToString(" +\n\t") { "\"/{${provideSerializeSnipped(it)}}\"" }

        if(optionalParameters.none()) return body

        return "$body+\n\t\"?" + optionalParameters.joinToString(" +\n\t\"&") {
            "${it.name}=${provideSerializeSnipped(it)}\""
        }
    }

    private fun provideSerializeSnipped(parameter: Parameter): String  {
        return "\${${parameter.navArgType.actualTypeSimpleName}.serializeValue(${parameter.name})}"
    }
}