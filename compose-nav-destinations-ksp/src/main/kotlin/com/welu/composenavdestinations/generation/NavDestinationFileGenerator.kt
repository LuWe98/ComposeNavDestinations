package com.welu.composenavdestinations.generation

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.NavDestinationInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterTypeInfo
import com.welu.composenavdestinations.utils.PackageUtils

class NavDestinationFileGenerator(
    private val fileOutputWriter: FileOutputWriter
) : FileContentGenerator<NavDestinationInfo> {

    override fun generate(instance: NavDestinationInfo) {
        if (instance.parameters.isEmpty()) {
            generatePlainSpecFileContentInfo(instance)
        } else {
            generateArgSpecFileContentInfo(instance)
        }.let(fileOutputWriter::writeFile)
    }

    private fun generatePlainSpecFileContentInfo(instance: NavDestinationInfo): FileContentInfo = FileContentInfo(
        packageDir = instance.packageName,
        imports = listOf(
            PackageUtils.NAV_DEEP_LINK_IMPORT,
            PackageUtils.NAV_DESTINATION_PLAIN_SPEC_IMPORT
        ),
        code = Templates.NAV_DESTINATION_PLAIN_SPEC_TEMPLATE
            .replace(Templates.PLACEHOLDER_BASE_ROUTE, instance.route)
            .replace(Templates.PLACEHOLDER_DESTINATION_NAME, instance.name)
            //TODO -> Das noch schauen mit deep Links
            .replace(Templates.PLACEHOLDER_DEEPLINK_VALUE, "emptyList()"),
        fileName = instance.name
    )

    private fun generateArgSpecFileContentInfo(instance: NavDestinationInfo): FileContentInfo {
        val sortedParams = instance.parameters.sortedWith(compareBy(Parameter::typeInfo / ParameterTypeInfo::isNullable, Parameter::hasDefaultValue))
        val navArgsClassName = instance.navArgsClass?.simpleName?.asString() ?: "${instance.name}.NavArgs"

        return FileContentInfo(
            packageDir = instance.packageName,
            imports = instance.allImports.toMutableList().apply {
                add(PackageUtils.NAV_DEEP_LINK_IMPORT)
                add(PackageUtils.NAV_DESTINATION_ARG_SPEC_IMPORT)
                add(PackageUtils.NAV_BACK_STACK_ENTRY_IMPORT)
                add(PackageUtils.NAV_ARGUMENT_IMPORT)
                add(PackageUtils.NAMED_NAV_ARGUMENT_IMPORT)
                add(PackageUtils.SAVED_STATE_HANDLE_IMPORT)
                add(PackageUtils.NAV_DESTINATION_ROUTABLE_IMPORT)
            },
            code = Templates.NAV_DESTINATION_ARG_SPEC_TEMPLATE
                .replace(Templates.PLACEHOLDER_BASE_ROUTE, instance.route)
                .replace(Templates.PLACEHOLDER_DESTINATION_NAME, instance.name)
                .replace(Templates.PLACEHOLDER_NAV_ARG_TYPE, navArgsClassName)
                .replace(Templates.PLACEHOLDER_GENERATED_NAV_ARG_TYPE, generateCustomNavArgClass(instance))
                .replace(Templates.PLACEHOLDER_ROUTE_ARGS, generateRouteArgs(sortedParams))
                .replace(Templates.PLACEHOLDER_INVOKE_FUNCTION_PARAMETER, generateInvokeParameters(instance))
                .replace(Templates.PLACEHOLDER_INVOKE_FUNCTION_BODY, generateInvokeBody(instance.route, sortedParams))
                .replace(Templates.PLACEHOLDER_NAMED_ARGUMENTS, generateNamedNavArgs(sortedParams))
                .replace(Templates.PLACEHOLDER_GET_ARGS_BACKSTACK, generateGetArgsBody(instance, true))
                .replace(Templates.PLACEHOLDER_GET_ARGS_SAVED_STATE, generateGetArgsBody(instance, false))
                //TODO -> Das noch schauen mit deep Links
                .replace(Templates.PLACEHOLDER_DEEPLINK_VALUE, "emptyList()"),
            fileName = instance.name
        )
    }

    private fun generateCustomNavArgClass(instance: NavDestinationInfo): String {
        if (instance.navArgsClass != null) return ""
        val args = instance.parameters.joinToString(",\n\t\t") {
            "val " + it.fullDeclarationName
        }
        return "data class NavArgs(\n\t\t$args\n\t)".trimIndent()
    }

    private fun generateRouteArgs(sortedParams: List<Parameter>): String {
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

    private fun generateInvokeBody(routeName: String, sortedParams: List<Parameter>): String {
        var optionalNavSeparator = '?'
        return "\"$routeName\" +\n\t\t" + sortedParams.joinToString(" +\n\t\t") {
            val serializeSnipped = "\${${it.navArgInfo.simpleName}.serializeValue(${it.name})}"
            if (it.typeInfo.isNullable || it.hasDefaultValue) {
                "\"$optionalNavSeparator${it.name}=$serializeSnipped\"".also {
                    optionalNavSeparator = '&'
                }
            } else {
                "\"/$serializeSnipped\""
            }
        }
    }

    private fun generateInvokeParameters(instance: NavDestinationInfo) = instance.parameters.joinToString(",\n\t\t", transform = Parameter::fullDeclarationName)

    private fun generateNamedNavArgs(sortedParams: List<Parameter>) = sortedParams.joinToString(",\n\t\t") { parameter ->
        val defaultValue = parameter.defaultValue?.let { ", ${it.value}" } ?: ""
        "navArgument(\"${parameter.name}\", ${parameter.navArgInfo.simpleName}$defaultValue,${parameter.typeInfo.isNullable})"
    }

    private fun generateGetArgsBody(instance: NavDestinationInfo, isBackStackEntry: Boolean): String {
        val calledOnInstance = if(isBackStackEntry) "navBackStackEntry" else "savedStateHandle"
        return instance.parameters.joinToString(",\n\t\t") {
            val nonNullableClaim = if(it.typeInfo.isNullable) "" else "!!"
            it.name + " = " + "${it.navArgInfo.simpleName}.getTyped($calledOnInstance, \"${it.name}\")$nonNullableClaim"
        }
    }
}