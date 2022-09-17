package com.welu.composenavdestinations.generation.destinations

import com.welu.composenavdestinations.extensions.div
import com.welu.composenavdestinations.generation.FileContentInfoGenerator
import com.welu.composenavdestinations.model.FileContentInfo
import com.welu.composenavdestinations.model.Parameter
import com.welu.composenavdestinations.model.ParameterTypeInfo
import com.welu.composenavdestinations.model.components.NavDestinationInfo
import com.welu.composenavdestinations.utils.PackageUtils

object FileGeneratorDestinationSpec : FileContentInfoGenerator<NavDestinationInfo> {

    override fun generate(instance: NavDestinationInfo): FileContentInfo = if (instance.isArgDestination) {
        generateArgSpecFileContentInfo(instance)
    } else {
        generatePlainSpecFileContentInfo(instance)
    }

    private fun generatePlainSpecFileContentInfo(plainDestinationInfo: NavDestinationInfo): FileContentInfo = FileContentInfo(
        fileName = plainDestinationInfo.simpleName,
        packageDir = plainDestinationInfo.packageDir,
        imports = setOf(
            plainDestinationInfo.destinationImport,
            PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
            PackageUtils.NAV_DESTINATION_PLAIN_SPEC_IMPORT,
            plainDestinationInfo.parentNavGraphSpecImport
        ),
        code = NavDestinationCodeTemplates.NAV_DESTINATION_PLAIN_SPEC_TEMPLATE
            .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_BASE_ROUTE, plainDestinationInfo.baseRoute)
            .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DESTINATION_NAME, plainDestinationInfo.simpleName)
            .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAME, plainDestinationInfo.destinationImport.simpleName)
            .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH, plainDestinationInfo.parentNavGraphSpecImport.simpleName)
            //TODO -> Das noch schauen mit deep Links
            .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE, "emptyList()")
    )

    private fun generateArgSpecFileContentInfo(argDestinationInfo: NavDestinationInfo): FileContentInfo {
        val sortedParams = argDestinationInfo.navArgsInfo!!.parameters.sortedWith(compareBy(Parameter::typeInfo / ParameterTypeInfo::isNullable, Parameter::hasDefaultValue))

        return FileContentInfo(
            packageDir = argDestinationInfo.packageDir,
            imports = mutableSetOf(
                argDestinationInfo.parentNavGraphSpecImport,
                PackageUtils.ANDROID_NAVIGATION_DEEP_LINK_IMPORT,
                PackageUtils.NAV_DESTINATION_ARG_SPEC_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAV_BACK_STACK_ENTRY_IMPORT,
                PackageUtils.NAV_ARGUMENT_IMPORT,
                PackageUtils.ANDROID_NAVIGATION_NAMED_NAV_ARGUMENT_IMPORT,
                PackageUtils.SAVED_STATE_HANDLE_IMPORT,
                PackageUtils.ROUTABLE_IMPORT
            ).apply {
                addAll(argDestinationInfo.allImports)
            },
            code = NavDestinationCodeTemplates.NAV_DESTINATION_ARG_SPEC_TEMPLATE
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_BASE_ROUTE, argDestinationInfo.baseRoute)
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAME, argDestinationInfo.destinationImport.simpleName)
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_DESTINATION_NAV_GRAPH, argDestinationInfo.parentNavGraphSpecImport.simpleName)
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DESTINATION_NAME, argDestinationInfo.simpleName)
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAV_ARG_TYPE, argDestinationInfo.navArgsInfo.name)
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_ROUTE_ARGS, generateRouteArgs(sortedParams))
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_PARAMETER, generateInvokeParameters(argDestinationInfo))
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_INVOKE_FUNCTION_BODY, generateInvokeBody(argDestinationInfo.baseRoute, sortedParams))
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_NAMED_ARGUMENTS, generateNamedNavArgs(sortedParams))
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_BACKSTACK, generateGetArgsBody(argDestinationInfo, true))
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GET_ARGS_SAVED_STATE, generateGetArgsBody(argDestinationInfo, false))
                //TODO -> Das noch schauen mit deep Links
                .replace(NavDestinationCodeTemplates.PLACEHOLDER_NAV_SPEC_DEEPLINK_VALUE, "emptyList()"),
            fileName = argDestinationInfo.simpleName
        )
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
            val serializeSnipped = "\${${it.navArgTypeInfo.simpleName}.serializeValue(${it.name})}"
            if (it.typeInfo.isNullable || it.hasDefaultValue) {
                "\"$optionalNavSeparator${it.name}=$serializeSnipped\"".also {
                    optionalNavSeparator = '&'
                }
            } else {
                "\"/$serializeSnipped\""
            }
        }
    }

    private fun generateInvokeParameters(instance: NavDestinationInfo) = instance
        .navArgsInfo!!
        .parameters
        .joinToString(",\n\t\t", transform = Parameter::fullDeclarationName)

    private fun generateNamedNavArgs(sortedParams: List<Parameter>) = sortedParams.joinToString(",\n\t\t") { parameter ->
        val defaultValue = parameter.defaultValue?.let { ", ${it.value}" } ?: ""
        "navArgument(\"${parameter.name}\", ${parameter.navArgTypeInfo.simpleName}$defaultValue,${parameter.typeInfo.isNullable})"
    }

    private fun generateGetArgsBody(instance: NavDestinationInfo, isBackStackEntry: Boolean): String {
        val calledOnInstance = if (isBackStackEntry) "navBackStackEntry" else "savedStateHandle"
        return instance.navArgsInfo!!.parameters.joinToString(",\n\t\t") {
            val nonNullableClaim = if (it.typeInfo.isNullable) "" else "!!"
            it.name + " = " + "${it.navArgTypeInfo.simpleName}.getTyped($calledOnInstance, \"${it.name}\")$nonNullableClaim"
        }
    }


    //    //.replace(CodeTemplates.PLACEHOLDER_NAV_ARG_SPEC_GENERATED_NAV_ARG, generateCustomNavArgClass(instance))
//    private fun generateCustomNavArgClass(instance: NavDestinationInfo): String {
//        if (instance.navArgsInfo != null) return ""
//        val args = instance.navArgsInfo?.parameters?.joinToString(",\n\t\t") {
//            "val " + it.fullDeclarationName
//        }
//        return "data class NavArgs(\n\t\t$args\n\t)".trimIndent()
//    }

}